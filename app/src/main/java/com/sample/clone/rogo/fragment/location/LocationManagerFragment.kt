package com.sample.clone.rogo.fragment.location

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.sample.clone.rogo.R
import com.sample.clone.rogo.adapter.location.LocationListAdapter
import com.sample.clone.rogo.dialog.common.ErrorDialog
import com.sample.clone.rogo.dialog.location.EditLocationDialog
import com.sample.clone.rogo.dialog.location.NewLocationDialog
import rogo.iot.module.rogocore.sdk.SmartSdk
import rogo.iot.module.rogocore.sdk.entity.IoTLocation

class LocationManagerFragment: Fragment() {

    private var locationAdapter: LocationListAdapter? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_locationmanager, container, false)
        locationAdapter = LocationListAdapter(SmartSdk.locationHandler().all, object: (IoTLocation) -> Unit {
            override fun invoke(location: IoTLocation) {
                SmartSdk.setAppLocation(location.uuid)
                Navigation.findNavController(view).navigate(R.id.action_to_generalFragment)
            }
        }, object: (IoTLocation) -> Unit {
            override fun invoke(location: IoTLocation) {
                val editLocationDialog = EditLocationDialog(location, object: (Boolean, IoTLocation?) -> Unit {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun invoke(success: Boolean, location: IoTLocation?) {
                        if (success) {
                            val locationRecyclerView = view.findViewById<RecyclerView>(R.id.location_recycler_view)
                            val locationAdapter = locationRecyclerView.adapter as LocationListAdapter
                            locationAdapter.updateLocation(location!!)
                            locationAdapter.notifyDataSetChanged()
                        }
                        else {
                            val errorDialog = ErrorDialog("Error", "Failed to update location")
                            errorDialog.show(parentFragmentManager, "ErrorDialog")
                        }
                    }

                }, object: (Boolean) -> Unit {
                    @SuppressLint("NotifyDataSetChanged")
                    override fun invoke(success: Boolean) {
                        if (success) {
                            val locationRecyclerView = view.findViewById<RecyclerView>(R.id.location_recycler_view)
                            val locationAdapter = locationRecyclerView.adapter as LocationListAdapter
                            locationAdapter.notifyDataSetChanged()
                        }
                        else {
                            val errorDialog = ErrorDialog("Error", "Failed to delete location")
                            errorDialog.show(parentFragmentManager, "ErrorDialog")
                        }
                    }
                })
                editLocationDialog.show(parentFragmentManager, "LocationDialog")
            }
        })

        val locationRecyclerView = view.findViewById<RecyclerView>(R.id.location_recycler_view)
        locationRecyclerView.adapter = locationAdapter
        locationRecyclerView.layoutManager = LinearLayoutManager(context)

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val createButton = view.findViewById<FloatingActionButton>(R.id.create_location_button)

        createButton.setOnClickListener {
            val newLocationDialog = NewLocationDialog(object: (Boolean, IoTLocation?) -> Unit {
                @SuppressLint("NotifyDataSetChanged")
                override fun invoke(success: Boolean, location: IoTLocation?) {
                    if (success) {
                        val locationRecyclerView = view.findViewById<RecyclerView>(R.id.location_recycler_view)
                        val locationAdapter = locationRecyclerView.adapter as LocationListAdapter
                        locationAdapter.addLocation(location!!)
                        locationAdapter.notifyDataSetChanged()
                    }
                    else {
                        val errorDialog = ErrorDialog("Error", "Failed to create new location")
                        errorDialog.show(parentFragmentManager, "ErrorDialog")
                    }
                }

            })
            newLocationDialog.show(parentFragmentManager, "LocationDialog")
        }
    }
}