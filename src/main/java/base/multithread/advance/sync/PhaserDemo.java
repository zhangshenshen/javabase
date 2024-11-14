package main.java.base.multithread.advance.sync;

import java.util.Random;
import java.util.concurrent.Phaser;

public class PhaserDemo {

    static class PerTaskThread implements Runnable {

        private String taskName;
        private Phaser phaser;

        public PerTaskThread(String taskName, Phaser phaser) {
            this.taskName = taskName;
            this.phaser = phaser;
        }

        @Override
        public void run() {

            for (int i = 1; i < 4; i++) {

                try {
                    if (i >= 2 && taskName.equals("加载新手教程")) {
                        continue;
                    }
                    Random random = new Random();
                    Thread.sleep(1000 + random.nextInt(1000));
                    System.out.printf("关卡%d,需要加载%d个模块，当前模块【%s】%n",i,
                            phaser.getRegisteredParties(),taskName);

                    if (i == 1 && "加载新手教程".equals(taskName)) {
                        System.out.println("下次关卡移除加载【新手教程】模块");
                        phaser.arriveAndDeregister(); // 移除一个模块
                    } else {
                        phaser.arriveAndAwaitAdvance();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public static void main(String[] args) {

        Phaser phaser = new Phaser(4) {
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                System.out.printf("第%d次关卡准备完成%n", phase + 1);
                return phase == 3 || registeredParties == 0;
            }
        };

        new Thread(new PerTaskThread("加载地图数据", phaser)).start();
        new Thread(new PerTaskThread("加载人物模型", phaser)).start();
        new Thread(new PerTaskThread("加载背景音乐", phaser)).start();
        new Thread(new PerTaskThread("加载新手教程", phaser)).start();

    }
}
