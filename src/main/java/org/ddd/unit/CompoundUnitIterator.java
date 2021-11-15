package org.ddd.unit;

import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

/**
 * @author chenjx
 */
public class CompoundUnitIterator implements Iterator<Unit> {
    private Deque<Iterator<Unit>> stack = new LinkedList<>();

    public CompoundUnitIterator(Iterator<Unit> iterator) {
        stack.push(iterator);
    }

    @Override
    public Unit next() {
        if (hasNext()) {
            Iterator<Unit> iterator = stack.peek();
            Unit next = iterator.next();
            if (next instanceof  CompoundUnit) {
                stack.push(next.iterator());
                return next();
            }
            return next;
        }
        return null;
    }

    @Override
    public boolean hasNext() {
        if (stack.isEmpty()) {
            return false;
        }
        Iterator<Unit> iterator = stack.peek();
        if (iterator.hasNext()) {
            return true;
        }else {
            stack.pop();
            return hasNext();
        }
    }
}
