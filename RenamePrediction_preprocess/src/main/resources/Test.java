package compare;

public class Test {
    public static void main(String[] args) {
        String s = "a<-b<-";
        System.out.println(s.split("<-").length);

    }

    public void _() throws Exception {
        MetricsContainerImpl container = new MetricsContainerImpl("any");
        MetricsEnvironment.setCurrentContainer(container);
        timerInternals.advanceInputWatermark(new Instant(BoundedWindow.TIMESTAMP_MAX_VALUE));
        timerInternals.advanceOutputWatermark(new Instant(BoundedWindow.TIMESTAMP_MAX_VALUE));
        DoFn<KV<String, Integer>, Integer> fn = new MyDoFn();
        DoFnRunner<KV<String, Integer>, Integer> runner = DoFnRunners.defaultStatefulDoFnRunner(fn, getDoFnRunner(fn), WINDOWING_STRATEGY, new StatefulDoFnRunner.TimeInternalsCleanupTimer(timerInternals, WINDOWING_STRATEGY), new StatefulDoFnRunner.StateInternalsStateCleaner<>(fn, stateInternals, (Coder) WINDOWING_STRATEGY.getWindowFn().windowCoder()));
        runner.startBundle();
        IntervalWindow window = new IntervalWindow(new Instant(0), new Instant(0L + WINDOW_SIZE));
        Instant timestamp = new Instant(0);
        runner.processElement(WindowedValue.of(KV.of("hello", 1), timestamp, window, PaneInfo.NO_FIRING));
        long droppedValues = container.getCounter(MetricName.named(StatefulDoFnRunner.class, StatefulDoFnRunner.DROPPED_DUE_TO_LATENESS_COUNTER)).getCumulative();
        assertEquals(1L, droppedValues);
        runner.finishBundle();
    }
}
