
public class Queue implements QueueInterface {
  private Node lastNode;
  
  public Queue() {
    lastNode = null;   
  }  // end default constructor
  
  // queue operations:
  public boolean isEmpty() {
        return (lastNode == null);
  }  // end isEmpty

  public void dequeueAll() {
   lastNode = null;

  }  // end dequeueAll

  public void enqueue(Object newItem) {
    Node newNode = new Node(newItem);
    // insert the new node
    if (isEmpty()) {
      // insertion into empty queue
      newNode.setNext(newNode);
    }
    else {
      // insertion into nonempty queue
      newNode.setNext(lastNode.getNext());
      lastNode.setNext(newNode);
    }  // end if
    lastNode = newNode;

  }  // end enqueue

  public Object dequeue() throws QueueException {
    if (!isEmpty()) {
      // queue is not empty; remove front
      Node testNode = lastNode.getNext();
      if (testNode == lastNode)
      {
        lastNode = null;
        return testNode.getItem();
      }
      else
      {
        lastNode.setNext(testNode.getNext());
        return testNode.getItem();
      }
    }  //end if
    else {
      throw new QueueException("QueueException on dequeue:"
                             + "queue empty");
    }  // end if
  }  // end dequeue

  public Object front() throws QueueException {
    if (!isEmpty()) {
      Node firstNode = lastNode.getNext();
      return firstNode.getItem();
    }
    else {
      throw new QueueException("QueueException on front:"
                             + "queue empty");
    }
  }

  public Object clone() throws CloneNotSupportedException
  {
	boolean copied = false;
        Queue copy = new Queue();
        Node curr = lastNode, prev = null;
        while ( (! copied) && (lastNode != null) )
        {
                Node temp = new Node(curr.getItem());
                if (prev == null)
                        copy.lastNode = temp;
                else
                        prev.setNext(temp);
                prev = temp;
                curr = curr.getNext();
		copied = (lastNode == curr);
        }
	prev.setNext(copy.lastNode);
        return copy;
  }
} // end Queue
