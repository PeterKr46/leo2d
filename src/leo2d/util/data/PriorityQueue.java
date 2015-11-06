package leo2d.util.data;

import java.util.ArrayList;

/**
 * Created by peter on 7/19/15.
 */
public class PriorityQueue<E> {
    private Holder first;
    private ArrayList<Holder> others = new ArrayList<Holder>();

    public E peek() {
        return first != null ? first.content : null;
    }

    public E dequeue() {
        E result = first.content;
        if(others.size() == 0) {
        	first = null;
        } else {
        	first = others.remove(0);
        }
        return result;
    }

    public void enqueue(E content, double priority) {
        Holder holder = new Holder();
        holder.content = content;
        holder.priority = priority;
        if(first == null || first.priority > priority) {
            if(first != null) {
                others.add(0, first);
            }
            first = holder;
            return;
        }
        int i = 0;
        while( i < others.size() && holder.compareTo(others.get(i)) == 1) {
            i++;
        }
        others.add(i, holder);
    }

    public void remove(E obj) {
        if(first != null && first.content == obj) {
            first = others.remove(0);
        } else if(first != null) {
            for(int i = 0; i < others.size(); i++) {
                if(others.get(i).content == obj) {
                    others.remove(i);
                }
            }
        }
    }

    private class Holder implements Comparable<Holder> {
        E content;
        double priority;

        @Override
        public int compareTo(Holder holder) {
            return (holder.priority > priority ? 0 : 1);
        }

        public String toString() {
            return "H(" + priority + ", " + content.getClass().getSimpleName() + ")";
        }
    }

    public Object[] toArray() {
        Object[] content = new Object[others.size() + (first != null ? 1 : 0)];
        if(content.length > 0) {
            content[0] = first.content;
            if(content.length > 1) {
                for(int i = 0; i < others.size(); i++) {
                    content[i+1] = others.get(i).content;
                }
            }
        }
        return content;
    }

    public String toString() {
        String res = "PQ(";
        res += first.toString() + ", ";
        for(Holder h : others) {
            res += h + ", ";
        }
        res += ")";
        return res;
    }
}
