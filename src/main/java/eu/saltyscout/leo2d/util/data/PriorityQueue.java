package eu.saltyscout.leo2d.util.data;

import java.util.Arrays;

/**
 * Created by peter on 7/19/15.
 */
public class PriorityQueue<E> {
    private java.util.PriorityQueue<Holder> backend;

    public PriorityQueue() {
        backend = new java.util.PriorityQueue<>();
    }

    public E peek() {
        return backend.peek().content;
    }

    public E dequeue() {
        return backend.poll().content;
    }

    public Holder dequeueHolder() {
        return backend.poll();
    }

    public void enqueue(E content, double priority) {
        backend.add(new Holder(priority, content));
    }

    public void remove(E obj) {
        backend.removeIf(holder -> holder.content.equals(obj));
    }

    public int size() {
        return backend.size();
    }


    public Object[] toArray() {
        Object[] arr = backend.toArray();
        Arrays.sort(arr);
        for (int i = 0; i < arr.length; i++) {
            arr[i] = ((Holder) arr[i]).content;
        }
        return arr;
    }

    public String toString() {
        return "PQ(" + size() + ")";
    }

    public class Holder implements Comparable<Holder> {
        public E content;
        public double priority;

        private Holder() {

        }

        public Holder(double priority, E content) {
            this.priority = priority;
            this.content = content;
        }

        @Override
        public int compareTo(Holder holder) {
            return Double.compare(holder.priority, priority);
        }

        public String toString() {
            return "H(" + priority + ", " + content.getClass().getSimpleName() + ")";
        }
    }
}
