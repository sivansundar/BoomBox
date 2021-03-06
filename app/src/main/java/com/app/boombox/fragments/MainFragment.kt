package com.app.boombox.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.app.boombox.R
import com.app.boombox.databinding.FragmentMainBinding
import com.app.boombox.models.Song
import com.app.boombox.view.AlbumAdapter
import com.app.boombox.view.SongAdapter
import com.app.boombox.viewmodels.SongsViewModel
import timber.log.Timber

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [MainFragment.newInstance] factory method to
 * create an instance of this fragment.
 *
 *
 * This is also the Overview Fragment. Consists of Populars this week and Top Songs.
 *
 * Populars this week - consists of 5 random albums from your device as recommendations
 * Top Songs - lists out the top 10 songs based on the number of times they have been played.
 *
 */
class MainFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var rootBinding: FragmentMainBinding
    private lateinit var viewModel: SongsViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rootBinding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main, container, false
        )

        rootBinding.lifecycleOwner = this
        return rootBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val (list, adapter) = initTopAlbumsAdapter()
        initTopAlbumsViewModel(list, adapter)

        val (topSongList, songadapter) = initTop10SongsAdapter()
        initTopSongsViewModel(topSongList, songadapter)

    }

    private fun initTop10SongsAdapter(): Pair<ArrayList<Song>, SongAdapter> {
        val list = ArrayList<Song>()
        val adapter = SongAdapter(list, context)

        rootBinding.top10SongsRecyclerView.adapter = adapter

        return Pair(list, adapter)
    }


    private fun initTopAlbumsAdapter(): Pair<ArrayList<Song>, AlbumAdapter> {
        val list = ArrayList<Song>()
        val adapter = AlbumAdapter(list, context)

        rootBinding.populatAlbumsRecyclerView.adapter = adapter

        return Pair(list, adapter)
    }


    private fun initTopAlbumsViewModel(
        list: ArrayList<Song>,
        adapter: AlbumAdapter
    ) {
        viewModel = ViewModelProviders.of(this).get(SongsViewModel::class.java)
        viewModel.popularAlbums.observe(viewLifecycleOwner, Observer { item ->
            Timber.i(" Viewmodel item size : ${item.size}")
            rootBinding.populatAlbumsRecyclerView.also {
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
                adapter.notifyDataSetChanged()
            }
            list.addAll(item)
        })

    }

    private fun initTopSongsViewModel(
        list: ArrayList<Song>,
        adapter: SongAdapter
    ) {


        viewModel.top10Songs.observe(viewLifecycleOwner, Observer { item ->
            Timber.i(" Top 10 songs item size : ${item.size}")

            rootBinding.top10SongsRecyclerView.also {
                it.layoutManager =
                    LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
                adapter.notifyDataSetChanged()
            }

            list.addAll(item)
        })

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MainFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MainFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}
