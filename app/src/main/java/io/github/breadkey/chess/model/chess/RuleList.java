package io.github.breadkey.chess.model.chess;

import java.util.ArrayList;

public class RuleList extends ArrayList<ChessRuleManager.Rule> {
    @Override
    public boolean add(ChessRuleManager.Rule rule) {
        if (rule == null) {
            return false;
        }
        return super.add(rule);
    }
}
