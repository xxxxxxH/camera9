package wuye.you.min;

import android.graphics.Bitmap;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;

import org.jetbrains.annotations.NotNull;

import wuye.you.min.base.BaseActivity;
import wuye.you.min.databinding.ActivityCameraResultBinding;

public class CameraResultActivity extends BaseActivity<ActivityCameraResultBinding> {

    @NotNull
    @Override
    protected ActivityCameraResultBinding getViewBinding() {
        return ActivityCameraResultBinding.inflate(getLayoutInflater());
    }

    @Override
    public void initialization() {
        String imageUrl = getIntent().getStringExtra("imageUrl");
        if (imageUrl != null) {
            Glide.with(this).asBitmap().load(imageUrl).into(new SimpleTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull @NotNull Bitmap resource,
                                            @Nullable @org.jetbrains.annotations.Nullable Transition<? super Bitmap> transition) {
                    activityBinding.iv.setImageBitmap(resource);
                }
            });
        }
        activityBinding.cancel.setOnClickListener(view -> finish());
        activityBinding.confirm.setOnClickListener(view -> finish());
    }
}