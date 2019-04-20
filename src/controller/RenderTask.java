package controller;

import javafx.concurrent.Task;

class RenderTask extends Task<Integer> {

    private final int from, to;
    private final IMainController controller;

    RenderTask(int from, int to, IMainController controller) {
        this.from = from;
        this.to = to;
        this.controller = controller;
    }

    @Override
    protected Integer call() throws Exception {
        controller.setThread(from, to);
        return 0;
    }
}
