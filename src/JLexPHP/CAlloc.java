package JLexPHP;

/**
 * *************************************************************
 * Class: CAlloc ************************************************************
 */
class CAlloc {

    /**
     * *************************************************************
     * Function: newCDfa
     * ************************************************************
     */
    static JLexPHP.CDfa newCDfa(
            JLexPHP.CSpec spec
    ) {
        JLexPHP.CDfa dfa;

        dfa = new JLexPHP.CDfa(spec.m_dfa_states.size());
        spec.m_dfa_states.addElement(dfa);

        return dfa;
    }

    /**
     * *************************************************************
     * Function: newCNfaPair Description:
     * ************************************************************
     */
    static JLexPHP.CNfaPair newCNfaPair() {
        JLexPHP.CNfaPair pair = new JLexPHP.CNfaPair();

        return pair;
    }

    /**
     * *************************************************************
     * Function: newNLPair Description: return a new CNfaPair that matches a new
     * line: (\r\n?|[\n\uu2028\uu2029]) Added by CSA 8-Aug-1999, updated
     * 10-Aug-1999 ************************************************************
     */
    static JLexPHP.CNfaPair newNLPair(JLexPHP.CSpec spec) {
        JLexPHP.CNfaPair pair = newCNfaPair();
        pair.m_end = newCNfa(spec); // newline accepting state
        pair.m_start = newCNfa(spec); // new state with two epsilon edges
        pair.m_start.m_next = newCNfa(spec);
        pair.m_start.m_next.m_edge = JLexPHP.CNfa.CCL;
        pair.m_start.m_next.m_set = new JLexPHP.CSet();
        pair.m_start.m_next.m_set.add('\n');
        if (spec.m_dtrans_ncols - JLexPHP.CSpec.NUM_PSEUDO > 2029) {
            pair.m_start.m_next.m_set.add(2028);
            /*U+2028 is LS, the line separator*/
            pair.m_start.m_next.m_set.add(2029);
            /*U+2029 is PS, the paragraph sep.*/
        }
        pair.m_start.m_next.m_next = pair.m_end; // accept '\n', U+2028, or U+2029
        pair.m_start.m_next2 = newCNfa(spec);
        pair.m_start.m_next2.m_edge = '\r';
        pair.m_start.m_next2.m_next = newCNfa(spec);
        pair.m_start.m_next2.m_next.m_next = pair.m_end; // accept '\r';
        pair.m_start.m_next2.m_next.m_next2 = newCNfa(spec);
        pair.m_start.m_next2.m_next.m_next2.m_edge = '\n';
        pair.m_start.m_next2.m_next.m_next2.m_next = pair.m_end; // accept '\r\n';
        return pair;
    }

    /**
     * *************************************************************
     * Function: newCNfa Description:
     * ************************************************************
     */
    static JLexPHP.CNfa newCNfa(
            JLexPHP.CSpec spec
    ) {
        JLexPHP.CNfa p;

        /* UNDONE: Buffer this? */
        p = new JLexPHP.CNfa();

        /*p.m_label = spec.m_nfa_states.size();*/
        spec.m_nfa_states.addElement(p);
        p.m_edge = JLexPHP.CNfa.EPSILON;

        return p;
    }
}
