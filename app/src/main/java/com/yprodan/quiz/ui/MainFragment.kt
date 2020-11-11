package com.yprodan.quiz.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.yprodan.quiz.R
import com.yprodan.quiz.ui.adapter.ItemRecyclerViewAdapter
import com.yprodan.quiz.utils.Constants
import com.yprodan.quiz.utils.OnItemClickListener

/**
 * A fragment representing a list of Items.
 */
class MainFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_list_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fileNamesList = mutableListOf<String>()
        for (i in requireActivity().application?.assets?.list(Constants.FILE_FOLDER_NAME_TAG)!!) {
            //trims the file extension to display it in the list
            fileNamesList.add(i.substring(0, i.length - Constants.ABBREVIATION_TAG.length))
        }

        // Set the adapter
        with(view as RecyclerView) {
            layoutManager = LinearLayoutManager(context)
            adapter = ItemRecyclerViewAdapter(fileNamesList, object : OnItemClickListener {
                override fun onClick(fileName: String) {
                    val bundle = Bundle()
                    bundle.putString(Constants.FILE_NAME_TAG, fileName)
                    findNavController().navigate(R.id.action_mainFragment_to_quizFragment, bundle)
                }
            })
        }
    }
}