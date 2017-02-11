package com.daose.htninterview.fresco;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;

import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.BaseRepeatedPostProcessor;

import java.util.ArrayList;
import java.util.List;

public class CombineProcessors extends BaseRepeatedPostProcessor {
    List<BasePostprocessor> processors;

    public CombineProcessors(List<BasePostprocessor> processors){
        super();
        this.processors = processors;
    }

    @Override
    public void process(Bitmap dest, Bitmap src){
        Canvas canvas = new Canvas(dest);
        Paint paint = new Paint();
        canvas.drawBitmap(src, 0, 0, paint);

        for (BasePostprocessor processor : processors) {
            processor.process(dest, dest);
        }
    }

    public static class Builder {
        List<BasePostprocessor> processors = new ArrayList<BasePostprocessor>();
        public Builder add(BasePostprocessor processor) {
            processors.add(processor);
            return this;
        }

        public CombineProcessors build() {
            return new CombineProcessors(processors);
        }
    }
}
