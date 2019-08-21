package com.archer.taskexecution;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.function.Consumer;

public class HTMLRenderer {

    static void renderText(CharSequence source) {

    }

    static List<ImageInfo> scanForImageInfo(CharSequence sequence) {
        return Arrays.asList(new ImageInfo(), new ImageInfo());
    }

    static void renderImages(List<ImageData> imageDataList) {

    }

    static void renderImage(ImageData imageData) {

    }

    static class ImageData {}

    static class ImageInfo {
        ImageData downloadImage() {
            return new ImageData();
        }
    }

    /**
     * 顺序执行，先渲染文字，再渲染图片，
     * 显然，效率欠佳。
     */
    static class SingleThreadRenderer {
        void renderPage(CharSequence source) {
            renderText(source);
            List<ImageData> imageDataList = new ArrayList<>();
            for (ImageInfo imageInfo : scanForImageInfo(source)) {
                imageDataList.add(imageInfo.downloadImage());
            }
            renderImages(imageDataList);
        }
    }


    static class FutureRenderer {

        private final ExecutorService service = Executors.newCachedThreadPool();

        void renderPage(CharSequence source) {
            /**
             * 渲染文字和提取图片信息的工作可以很快完成
             */
            renderText(source);
            List<ImageInfo> imageInfos = scanForImageInfo(source);

            /**
             * 对于下载图片的工作就比较耗时，
             * 因此封装到单独的线程去下载，
             * 下载完了以后再渲染
             */
            Future<List<ImageData>> future = service.submit(new Callable<List<ImageData>>() {
                @Override
                public List<ImageData> call() throws Exception {
                    List<ImageData> imageDataList = new ArrayList<>();
                    imageInfos.forEach(new Consumer<ImageInfo>() {
                        @Override
                        public void accept(ImageInfo imageInfo) {
                            imageDataList.add(imageInfo.downloadImage());
                        }
                    });
                    return imageDataList;
                }
            });

            try {
                /**
                 * Future描述了任务的生命周期，并提供了相关方法
                 * 来获取任务的结果、取消任务，也可以查询任务的状态(被取消还是执行完了)。
                 * Future的生命周期是单向的，一旦结束，就永远停留在最后的状态上。
                 *
                 * 如果任务已完成，get会立即返回结果，如果仍在进行中，会阻塞当前线程直至完成，
                 * 若在执行过程中抛出了异常，会封装成ExecutionException再次抛出，此时可用getCause
                 * 获得原始的被封装的异常，如果任务被取消，则抛出CancellationException
                 */
                List<ImageData> imageDataList = future.get();
                renderImages(imageDataList);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                future.cancel(true);
            } catch (ExecutionException e) {

            }
        }
    }

    static class Renderer {
        private final ExecutorService service = Executors.newCachedThreadPool();

        void renderPage(CharSequence source) {
            /**
             * 将每张图片的下载继续分解成一个个独立的线程任务
             *
             * NOTE：大量相互独立且同类的任务进行并发处理，会将程序
             * 的任务量分配到不同的任务中，这样才能真正获得性能的提升。
             */
            List<ImageInfo> imageInfos = scanForImageInfo(source);
            CompletionService<ImageData> completionService = new ExecutorCompletionService<>(service);
            for (ImageInfo imageInfo : imageInfos) {
                completionService.submit(new Callable<ImageData>() {
                    @Override
                    public ImageData call() throws Exception {
                        return imageInfo.downloadImage();
                    }
                });
            }
            renderText(source);
            /**
             * Java就没有啥回调的形式吗？！
             */
            try {
                for (int i = 0; i < imageInfos.size(); ++i) {
                    Future<ImageData> future = completionService.take();
                    renderImage(future.get());
                }
            } catch (Exception e) {

            }
        }
    }
}
