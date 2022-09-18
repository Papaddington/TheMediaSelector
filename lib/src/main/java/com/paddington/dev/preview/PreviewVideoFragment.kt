package com.paddington.dev.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.paddington.dev.MediaSelectorConfig
import com.paddington.dev.R
import com.paddington.dev.bean.MediaEntity
import kotlinx.android.synthetic.main.base_ms_fragment_preview_video.*

/**
 * <pre>
 *     author : paddington
 *     e-mail : paddington.699@gmail.com
 *     time   : 2021/01/25
 *     desc   :
 *     version: 1.0
 * </pre>
 */
class PreviewVideoFragment : Fragment() {
    companion object {
        private const val KEY_MEDIA = "key_media"

        fun newInstance(mediaEntity: MediaEntity): PreviewVideoFragment {
            return PreviewVideoFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(KEY_MEDIA, mediaEntity)
                }
            }
        }
    }

    private lateinit var mIvCover: ImageView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.base_ms_fragment_preview_video, null, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initVideoView()
        //videoView 播放视频
        arguments?.apply {
            val mediaEntity = getParcelable<MediaEntity>(KEY_MEDIA)
            mediaEntity?.let {
                previewVideo(it)
            }
        }
    }

    private fun initVideoView() {
        videoPlayer.apply {
            backButton.visibility = View.GONE
            fullscreenButton.visibility = View.GONE
            titleTextView.visibility = View.GONE
            setIsTouchWiget(false)
            mIvCover = ImageView(requireContext())
            thumbImageView = mIvCover
        }
    }

    private fun previewVideo(media: MediaEntity) {
        videoPlayer.setUp(media.path, false, "")
        MediaSelectorConfig.getImageLoader().loadImage(requireContext(), media.path, mIvCover)
    }

    override fun onPause() {
        super.onPause()
        videoPlayer.onVideoPause()
    }

    override fun onResume() {
        super.onResume()
        videoPlayer.onVideoResume()
    }

    override fun onDestroyView() {
        videoPlayer.release()
        super.onDestroyView()
    }
}