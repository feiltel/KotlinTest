package com.nut2014.kotlintest.view

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

open class BaseFragment : Fragment() {
    private var flag: String = ""
    override fun onAttach(activity: Activity) {
        flag = this.javaClass.name
        println("$flag>>>>>>>onAttach>>")
        super.onAttach(activity)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        flag = this.javaClass.name
        println("$flag>>>>>>>onCreate>>")
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        flag = this.javaClass.name
        println("$flag>>>>>>>onCreateView>>")
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        flag = this.javaClass.name
        println("$flag>>>>>>>onViewCreated>>")
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        flag = this.javaClass.name
        println("$flag>>>>>>>onActivityCreated>>")
        super.onActivityCreated(savedInstanceState)
    }

    override fun onStart() {
        flag = this.javaClass.name
        println("$flag>>>>>>>onStart>>")
        super.onStart()
    }

    override fun onResume() {
        flag = Thread.currentThread().stackTrace[1].className
        println("$flag>>>>>>>onResume>>")
        super.onResume()
    }

    override fun onPause() {
        flag = this.javaClass.name
        println("$flag>>>>>>>onPause>>")
        super.onPause()
    }

    override fun onStop() {
        flag = this.javaClass.name
        println("$flag>>>>>>>onStop>>")
        super.onStop()
    }

    override fun onDestroyView() {
        flag = this.javaClass.name
        println("$flag>>>>>>>onDestroyView>>")
        super.onDestroyView()
    }

    override fun onDetach() {
        flag = this.javaClass.name
        println("$flag>>>>>>>onDetach>>")
        super.onDetach()
    }


}
