package org.algo.interpreter.handler;

import org.algo.interpreter.core.Algorithm;

public interface Handler {

    boolean handleEvent(Algorithm algorithm, int line, String fullLine);

}
