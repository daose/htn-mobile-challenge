package com.daose.htninterview.fresco;


import android.content.Context;
import android.net.Uri;

import com.daose.htninterview.R;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.backends.pipeline.PipelineDraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;

import jp.wasabeef.fresco.processors.BlurPostprocessor;
import jp.wasabeef.fresco.processors.ColorFilterPostprocessor;
import jp.wasabeef.fresco.processors.GrayscalePostprocessor;

public class FrescoTransform {
    public static void blur(Context ctx, SimpleDraweeView view, String url) {
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(Uri.parse(url))
                .setPostprocessor(new CombineProcessors.Builder()
                        .add(new BlurPostprocessor(ctx, 4))
                        .add(new ColorFilterPostprocessor(R.color.colorPrimaryDark))
                        .build())
                .build();

        PipelineDraweeController controller = (PipelineDraweeController)
                Fresco.newDraweeControllerBuilder()
                        .setImageRequest(request)
                        .setOldController(view.getController())
                        .build();
        view.setController(controller);
    }
}
