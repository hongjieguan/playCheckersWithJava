import java.util.Stack;


public class MementoCaretaker {

    private Stack<Memento> mementoStack;

    public MementoCaretaker(){
        mementoStack = new Stack<Memento>();
    }

    public Memento sendBoard(){
        int s = mementoStack.size();
        if (s>1){
            mementoStack.remove(s-1);
        }
        return mementoStack.peek();
    }

    public void save() {
        mementoStack.push(Board.save());
    }

    public Integer getSize(){
        return mementoStack.size();
    }


}
