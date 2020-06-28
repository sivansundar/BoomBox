package com.app.boombox

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter(fm: FragmentManager, behavior: Int) :
    FragmentPagerAdapter(fm, behavior) {

    private val listFragment: ArrayList<Fragment> = ArrayList()
    private val listFragmentTitles: ArrayList<String> = ArrayList()

    fun addFragment(fragment: Fragment, title: String) {
        listFragment.add(fragment)
        listFragmentTitles.add(title)
    }

    override fun getItem(position: Int): Fragment {
        return listFragment[position]
    }

    override fun getCount(): Int {
        return listFragment.size
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return listFragmentTitles.get(position)
    }


}
