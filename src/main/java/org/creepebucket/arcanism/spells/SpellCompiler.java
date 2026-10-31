package org.creepebucket.arcanism.spells;

import net.minecraft.world.Container;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;
import org.creepebucket.arcanism.registries.ModDataComponents;
import org.creepebucket.arcanism.registries.ModItems;
import org.creepebucket.arcanism.registries.SpellRegistry;
import org.creepebucket.arcanism.spells.api.SpellExceptions;
import org.creepebucket.arcanism.spells.api.SpellItemLogic;
import org.creepebucket.arcanism.spells.api.SpellSequence;
import org.creepebucket.arcanism.spells.spells_compute.*;

import java.lang.reflect.InvocationTargetException;
import java.util.*;

import static org.creepebucket.arcanism.spells.SpellValueType.NUMBER;

public class SpellCompiler {
    public static class SpellAst {
        public SpellItemLogic spell;
        public SpellAst lChild, rChild;

        public SpellAst(SpellItemLogic spell, SpellAst lChild, SpellAst rChild) {
            this.spell = spell;
            this.lChild = lChild;
            this.rChild = rChild;
        }

        public SpellAst(SpellItemLogic spell) {
            this.spell = spell;
        }

        @Override
        public String toString() {
            return flatten().toString();
        }

        public SpellSequence flatten() {
            if (spell == null) return new SpellSequence();
            var seq = new SpellSequence();
            if (lChild != null) seq.pushRight(lChild.flatten());
            if (rChild != null) seq.pushRight(rChild.flatten());
            seq.pushRight(spell);
            return seq;
        }
    }

    public List<SpellExceptions> errors = new ArrayList<>();
    public boolean skipCompile = false, skipCheck = false;

    public SpellSequence compile(Container spells) {

        // Step 1: List<ItemStack>转换为SpellSequence
        int id = 0;

        SpellSequence rawSequence = new SpellSequence();
        for (ItemStack spell : spells) {
            if (spell.isEmpty()) continue;
            else if (spell.is(ModItems.PACKED_SPELL)) {
                var compiler = new SpellCompiler();
                compiler.skipCompile = true;
                compiler.skipCheck = true;

                rawSequence.pushRight(compiler.compile(new SimpleContainer(spell.get(ModDataComponents.SPELLS).toArray(new ItemStack[1]))));
                continue;
            } else if (!SpellRegistry.isSpell(spell.getItem())) {
                var spellItemLogic = new ValueLiteralSpell(SpellValueType.ITEM, spell.copy(), "name");
                spellItemLogic.id = id; // 设置id 在调试时候用
                rawSequence.pushRight(spellItemLogic);
                continue;
            }

            var spellItemLogic = SpellRegistry.createSpellLogic(spell.getItem());
            spellItemLogic.id = id; // 设置id 在调试时候用
            rawSequence.pushRight(spellItemLogic);

            id++;
        }

        // Step 2: 编译前语法检查

        // 2.1: 检查括号等法术配对情况
        if (!skipCheck) {
            Map<Class<? extends SpellItemLogic.PairedLeftSpell>, Integer> pairsCount = new HashMap<>();

            for (SpellItemLogic spell = rawSequence.head; spell != null; spell = spell.next) {
                // 左括号为配对计数器+1
                if (spell instanceof SpellItemLogic.PairedLeftSpell left) {
                    try {
                        var leftType = left.rightSpellType.getDeclaredConstructor().newInstance().leftSpellType;
                        pairsCount.put(leftType, pairsCount.getOrDefault(leftType, 0) + 1);
                    } catch (InstantiationException e) { // 我操, 反射好恶心啊
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }

                // 右括号为配对计数器-1
                if (spell instanceof SpellItemLogic.PairedRightSpell right) {
                    // 如果计数器等于0, 直接报错未配对括号
                    if (pairsCount.getOrDefault(right.leftSpellType, 0) <= 0) {
                        errors.add(SpellExceptions.PAIRS_UNMATCHED(spell)); // 这个右括号永远不会配对
                    }

                    pairsCount.put(right.leftSpellType, pairsCount.getOrDefault(right.leftSpellType, 0) - 1);
                }
            }

            // 如果配对计数器任意一项不为0, 则报错未配对法术
            for (Class<? extends SpellItemLogic.PairedLeftSpell> leftSpell : pairsCount.keySet()) {
                if (pairsCount.get(leftSpell) != 0) {
                    try {
                        errors.add(SpellExceptions.PAIRS_UNMATCHED(leftSpell.getDeclaredConstructor().newInstance()));
                    } catch (InstantiationException e) { // 何意味
                        throw new RuntimeException(e);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    } catch (InvocationTargetException e) {
                        throw new RuntimeException(e);
                    } catch (NoSuchMethodException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }

        // Step 3: 解析逗号和数字

        SpellItemLogic p = rawSequence.tail; // 可爱的指针
        while (p != null) {
            if (p instanceof NumberDigitSpell digit) {
                // 是数字就把值替换整段数字法术
                List<Object> result = digit.run(null, null, null, null).returnValue;
                SpellItemLogic next = ((NumberDigitSpell) result.get(1)).prev;

                var valueLiteral = new ValueLiteralSpell(NUMBER, result.get(0));
                valueLiteral.id = p.id;
                rawSequence.replaceSection((NumberDigitSpell) result.get(1), p, new SpellSequence().pushRight(valueLiteral)); // 何意味
                p = next;
                continue;
            }

            if (p instanceof CommaSpell && skipCompile) { // 继续编译的话逗号有分割语句的语义, 不能删
                SpellItemLogic next = p.prev;
                rawSequence.replaceSection(p, p, new SpellSequence());
                p = next;
                continue;
            }
            p = p.prev;
        }

        // Step 4: 配对括号等法术

        // 需要配对的法术栈
        Map<Class<? extends SpellItemLogic.PairedLeftSpell>, ArrayDeque<SpellItemLogic>> pairs = new HashMap<>();
        for (SpellItemLogic i = rawSequence.head; i != null; i = i.next) {
            if (i instanceof SpellItemLogic.PairedLeftSpell left) {
                try {
                    var leftType = left.rightSpellType.getDeclaredConstructor().newInstance().leftSpellType;
                    // 左括号入栈
                    pairs.computeIfAbsent(leftType, k -> new ArrayDeque<>())
                            .push(i);
                } catch (InstantiationException e) { // 反射死妈了
                    throw new RuntimeException(e);
                } catch (IllegalAccessException e) {
                    throw new RuntimeException(e);
                } catch (InvocationTargetException e) {
                    throw new RuntimeException(e);
                } catch (NoSuchMethodException e) {
                    throw new RuntimeException(e);
                }
            }
            if (i instanceof SpellItemLogic.PairedRightSpell r) {
                // 右括号出栈并设置配对
                if (pairs.get(r.leftSpellType) == null || pairs.get(r.leftSpellType).peek() == null) continue;

                SpellItemLogic.PairedLeftSpell l = (SpellItemLogic.PairedLeftSpell) pairs.get(r.leftSpellType).pop();
                r.leftSpell = l;
                l.rightSpell = r;
            }
        }

        if (skipCompile) return rawSequence;

        // 括号等语法检查没过就不进解析
        if (!errors.isEmpty()) return rawSequence;

        // Step 5: 解析法术序列

        /*
         * 法术优先级:
         *
         *  ^ 先执行
         *  | +05 StorageSpell.GetStoreSpell (get_store)
         *  | +04 NumberOperationsSpell.ExponentSpell (exponent, 右结合)
         *  | +03 NumberOperationsSpell 一元运算 (如 sin/cos/tan/asin/acos/atan/...)
         *  | +02 NumberOperationsSpell 二元乘除模 (multiplication/division/remainder)
         *  | +01 NumberOperationsSpell 二元加减 (addition/subtraction)
         *  | 000 BoolOperationsSpell 比较/判定与默认优先级 (如 greater_than/less_than/equal_to/...)
         *  | -01 BoolOperationsSpell.NotSpell (not)
         *  | -02 BoolOperationsSpell.OrSpell (or)
         *  | -03 BoolOperationsSpell.AndSpell (and)
         *  | -04
         *  | -05
         *  | ...
         *  | -99 所有无输出普通法术
         *  v 后执行
         *
         */

        // Pratt parser
        var ast = parse(rawSequence, -9999);

        // Step 6: 编译后检查
        // TODO: 目前还没有

        return ast == null ? new SpellSequence() : ast.flatten();
    }

    /*
     * 判断 seq 的下一个法术是否会作为 spell 的右操作数
     * 一元算子后面紧跟同级或更低优先级的法术时, 应按后缀处理
     */
    public static boolean hasRightOperand(SpellSequence seq, SpellItemLogic spell) {
        if (seq.head == null) return false;
        double lbp = seq.head.rightConnectivity ? seq.head.precedence : seq.head.precedence + 0.1;
        return lbp > spell.precedence + 0.1;
    }

    public static SpellAst parse(SpellSequence seq, double min_binding_power) {
        SpellAst lhs = null, rhs = null;

        var firstSpell = seq.head != null ? seq.popLeft() : null;
        if (firstSpell instanceof ValueLiteralSpell) {
            lhs = new SpellAst(firstSpell);
        } else if (firstSpell instanceof NumberOperationsSpell.SubtractionSpell) {
            rhs = parse(seq, new NumberOperationsSpell.NegationSpell().precedence);
            var newSpell = new NumberOperationsSpell.NegationSpell();
            newSpell.id = firstSpell.id;
            lhs = new SpellAst(newSpell, rhs, null);
        } else if (firstSpell instanceof ParenSpell.LParenSpell) {
            lhs = parse(seq, -9999);
            if (seq.head instanceof ParenSpell.RParenSpell) seq.popLeft();
        } else if (firstSpell instanceof CommaSpell || firstSpell instanceof ParenSpell.RParenSpell) {
            // pass
        } else if (firstSpell != null && !firstSpell.bypassShunting && firstSpell.inputTypes.get(0).size() == 1 && firstSpell.inputTypes.get(0).get(0) != SpellValueType.EMPTY) {
            rhs = parse(seq, firstSpell.precedence);
            lhs = new SpellAst(firstSpell, rhs, null);
        } else {
            lhs = new SpellAst(firstSpell);
        }

        while (true) {
            var spell = seq.head;
            if (spell == null) {
                break;
            } else if (spell instanceof ParenSpell.RParenSpell) {
                break;
            }
            double lbp = spell.rightConnectivity ? spell.precedence : spell.precedence + 0.1;
            double rbp = spell.precedence;
            if (lbp <= min_binding_power) break;

            // 递归解析右子树
            seq.popLeft();

            // 逗号只做语句分隔, 不进最终序列
            if (spell instanceof CommaSpell) continue;

            // 处理不同法术
            if (spell instanceof ValueLiteralSpell || spell instanceof DynamicConstantSpell || spell.bypassShunting) {
                lhs = new SpellAst(spell, lhs, null);
            } else if (spell.inputTypes.get(0).size() == 1 && spell.inputTypes.get(0).get(0) != SpellValueType.EMPTY && !hasRightOperand(seq, spell)) {
                lhs = new SpellAst(spell, lhs, null);
            } else {
                rhs = parse(seq, rbp);
                lhs = new SpellAst(spell, lhs, rhs);
            }
        }

        return lhs;
    }
}
