package com.paddington.dev.preview

import android.graphics.PointF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.paddington.dev.MediaSelectorConfig
import com.paddington.dev.R
import com.paddington.dev.bean.MediaEntity
import com.paddington.dev.widget.longimage.ImageSource
import com.paddington.dev.widget.longimage.ImageViewState
import com.paddington.dev.widget.longimage.SubsamplingScaleImageView
import kotlinx.android.synthetic.main.base_ms_fragment_preview_picture.*

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PreviewPictureFragment : Fragment() {
    companion object {
        private const val KEY_MEDIA = "key_media"

        fun newInstance(mediaEntity: MediaEntity): PreviewPictureFragment {
            return PreviewPictureFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MEDIA, mediaEntity)
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_ms_fragment_preview_picture, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        longPhotoView.apply {
            isQuickScaleEnabled = true
            isZoomEnabled = true
            isPanEnabled = true
            setDoubleTapZoomDuration(100)
            setMinimumScaleType(SubsamplingScaleImageView.SCALE_TYPE_CENTER_CROP)
            setDoubleTapZoomDpi(SubsamplingScaleImageView.ZOOM_FOCUS_CENTER)
        }

        arguments?.apply {
            val mediaEntity = getParcelable<MediaEntity>(KEY_MEDIA)
            mediaEntity?.let {
                previewPicture(it)
            }
        }
    }

    private fun previewPicture(media: MediaEntity) {
        if (media.isLongPicture()) {
            photoView.isVisible = false
            longPhotoView.isVisible = true
            longPhotoView.setImage(
                ImageSource.uri(media.getUriPath()),
                ImageViewState(0f, PointF(0f, 0f), 0)
            )
        } else {
            photoView.isVisible = true
            longPhotoView.isVisible = false
            MediaSelectorConfig.getImageLoader().loadImage(requireContext(), media.path, photoView)
        }
    }
}