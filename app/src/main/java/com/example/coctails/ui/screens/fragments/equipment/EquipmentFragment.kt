package com.example.coctails.ui.screens.fragments.equipment

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.GridLayoutManager
import com.example.coctails.R
import com.example.coctails.interfaces.OnRecyclerItemClick
import com.example.coctails.core.room.entity.equipment_data.EquipmentFirebaseData
import com.example.coctails.ui.screens.BaseFragment
import com.example.coctails.ui.screens.activities.main.MainActivity
import com.example.coctails.ui.screens.fragments.equipment.adapters.EquipmentRecyclerViewAdapter
import com.example.coctails.ui.screens.fragments.equipment_details.EquipmentDetailsFragment
import com.example.coctails.utils.EQUIPMENT_ID
import kotlinx.android.synthetic.main.common_progress_bar.*
import kotlinx.android.synthetic.main.common_toolbar.*
import kotlinx.android.synthetic.main.fragment_equipment.*

class EquipmentFragment : BaseFragment<EquipmentPresenter, EquipmentView>(), EquipmentView, OnRecyclerItemClick{

    private var activity : MainActivity? = null
    private var mLayoutManager: GridLayoutManager? = null
    private var adapter: EquipmentRecyclerViewAdapter? = null

    override fun getLayoutId(): Int = R.layout.fragment_equipment

    override fun providePresenter(): EquipmentPresenter = EquipmentPresenterImpl()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        activity = context as MainActivity
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.bindView(this)
        presenter.getAllEquipment()

        setupRecycler()
    }

    override fun onResume() {
        super.onResume()
        commonToolbarTitle.text = getString(R.string.equipment)
        commonToolbarBackPress.setOnClickListener{ activity?.onBackPressed()}
    }

    private fun setupRecycler() {
        mLayoutManager = GridLayoutManager(context, 2)
        equipmentRecyclerView.layoutManager = mLayoutManager
        equipmentRecyclerView.setHasFixedSize(true)
        adapter = EquipmentRecyclerViewAdapter(this)
        equipmentRecyclerView.adapter = adapter
    }

    override fun showEquipment(equipmentFirebaseDataList: List<EquipmentFirebaseData>) {
        commonProgressBar.visibility = View.GONE
        adapter?.setList(equipmentFirebaseDataList)
    }

    override fun onItemClick(position: Int) {
        val fragment = EquipmentDetailsFragment()

        val bundle = Bundle()

        bundle.putInt(EQUIPMENT_ID, adapter?.getAdapterList()?.get(position)?.id!!)
        fragment.arguments = bundle

        activity?.loadFragment(fragment, "EquipmentDetails", true)
    }


    override fun onDestroyView() {
        super.onDestroyView()
        presenter.unbindView()
    }

}
