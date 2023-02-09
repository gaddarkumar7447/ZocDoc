package com.example.zocdoc.onboarding

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.AppCompatTextView
import com.example.zocdoc.R
import com.example.zocdoc.databinding.FragmentOnBindingFragmentsBinding


class OnBindingFragments : Fragment() {
    private var title: String? = null
    private var description: String? = null
    private var imageResource = 0
    private lateinit var tvTitle: AppCompatTextView
    private lateinit var tvDescription: AppCompatTextView
    private lateinit var image: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            title = requireArguments().getString(ARG_PARAM1)
            description = requireArguments().getString(ARG_PARAM2)
            imageResource = requireArguments().getInt(ARG_PARAM3)
        }
    }


    private var _binding : FragmentOnBindingFragmentsBinding?= null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentOnBindingFragmentsBinding.inflate(inflater, container, false)
        val view = binding.root

        tvTitle = binding.textOnboardingTitle
        tvDescription = binding.textOnboardingDescription
        tvTitle.text = title
        image = binding.imageOnboarding
        tvDescription.text = description
        image.setImageResource(imageResource)

        return view
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        private const val ARG_PARAM1 = "param1"
        private const val ARG_PARAM2 = "param2"
        private const val ARG_PARAM3 = "param3"
        fun newInstance(title: String?, description: String?, imageResource: Int): OnBindingFragments {
            val fragment = OnBindingFragments()
            val args = Bundle()
            args.putString(ARG_PARAM1, title)
            args.putString(ARG_PARAM2, description)
            args.putInt(ARG_PARAM3, imageResource)
            fragment.arguments = args
            return fragment
        }
    }

}