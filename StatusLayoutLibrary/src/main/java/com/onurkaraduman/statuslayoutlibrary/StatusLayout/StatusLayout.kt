package com.onurkaraduman.statuslayoutlibrary.StatusLayout

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.widget.FrameLayout
import com.onurkaraduman.statuslayoutlibrary.databinding.ViewStatusBinding

class StatusLayout @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private val binding = inflateCustomView(ViewStatusBinding::inflate)

    private var state: State = State.NONE //TODO (ONUR) don't forget this
    private var onErrorClicked: (() -> Unit) = {}


    init {
        binding.buttonStatus.setOnClickListener {
            onErrorClicked.invoke()
        }
    }

    fun setErrorClickListener(listener: (() -> Unit)) {
        onErrorClicked = listener
    }


    companion object {
        @JvmStatic
        fun provideLoadingStateInfo() = StateInfo(state = State.LOADING)

        @JvmStatic
        fun provideContentStateInfo() = StateInfo(state = State.CONTENT)

        @JvmStatic
        fun provideErrorStateInfo() = StateInfo(state = State.ERROR) //TODO (ONUR) don't forget this

        @JvmStatic
        fun provideLoadingWithContentStateInfo() = StateInfo(state = State.LOADING_WITH_CONTENT)

        @JvmStatic
        fun provideEmptyStateInfo() = StateInfo(state = State.EMPTY)

        @JvmStatic
        fun provideNoneStateInfo() = StateInfo(state = State.NONE) //TODO (ONUR) don't forget this
    }

    enum class State {
        LOADING, CONTENT, INFO, LOADING_WITH_CONTENT, ERROR, EMPTY, NONE
    }


    data class StateInfo(
        val infoImage: Int? = null,
        val infoTitle: String? = null,
        val infoMessage: String? = null,
        val infoButtonText: String? = null,
        val state: State = State.INFO,
        val onInfoButtonClick: (() -> Unit)? = null,
        val loadingAnimation: Animation? = null,
        val loadingWithContentAnimation: Animation? = null
    )

    //Visibility Functions
    fun showState(stateInfo: StateInfo?) {
        when (stateInfo?.state) {
            State.LOADING -> loading()
            State.CONTENT -> updateLoadingVisibility()
            State.LOADING_WITH_CONTENT -> updateVisibilityLoadingWithContent()
            State.INFO, State.ERROR, State.EMPTY -> {
                stateInfo.infoTitle?.let { infoTitle(it) }
                stateInfo.infoMessage?.let { infoMessage(it) }
                stateInfo.infoButtonText?.let { infoButtonText(it) }
                updateErrorVisibility()
            }

            null, State.NONE -> {}
        }
    }

    private fun setChildVisibility(visibility: Int) {
        val child = getChildAt(1)
        when (visibility) {
            View.VISIBLE -> child.visibility = View.VISIBLE
            else -> child.visibility = View.GONE
        }
    }

    private fun loading() {
        binding.apply {
            progressBarStatus.makeVisible()
            progressBarStateLayoutWithContent.makeGone()
            textViewStatusTitle.makeGone()
            textViewStatusDescription.makeGone()
            buttonStatus.makeGone()
        }
    }

    private fun updateLoadingVisibility() {
        binding.apply {
            progressBarStatus.makeGone()
            progressBarStateLayoutWithContent.makeGone()
            textViewStatusTitle.makeGone()
            textViewStatusDescription.makeGone()
            buttonStatus.makeGone()
        }
        setChildVisibility(View.VISIBLE)
    }

    private fun updateVisibilityLoadingWithContent() {
        binding.apply {
            progressBarStatus.makeGone()
            progressBarStateLayoutWithContent.makeVisible()
            textViewStatusTitle.makeGone()
            textViewStatusDescription.makeGone()
            buttonStatus.makeGone()
        }
    }

    private fun infoTitle(title: String) {
        binding.textViewStatusTitle.text = title
    }

    private fun infoMessage(message: String) {
        binding.textViewStatusDescription.text = message
    }

    private fun infoButtonText(message: String) {
        binding.buttonStatus.text = message
    }

    private fun updateErrorVisibility() {
        binding.apply {
            progressBarStatus.makeGone()
            progressBarStateLayoutWithContent.makeGone()
            textViewStatusTitle.makeVisible()
            textViewStatusDescription.makeVisible()
            buttonStatus.makeVisible()
        }
        setChildVisibility(View.GONE)
    }
}