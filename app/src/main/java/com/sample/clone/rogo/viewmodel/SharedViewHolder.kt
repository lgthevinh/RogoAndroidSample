package com.sample.clone.rogo.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import rogo.iot.module.platform.entity.IoTDirectDeviceInfo
import rogo.iot.module.rogocore.sdk.entity.IoTBleScanned
import rogo.iot.module.rogocore.sdk.entity.IoTDevice
import rogo.iot.module.rogocore.sdk.entity.IoTPairedRfDevice

class SharedViewHolder: ViewModel() {
    private var _deviceInfo: MutableLiveData<IoTDirectDeviceInfo> = MutableLiveData()
    val deviceInfoData: LiveData<IoTDirectDeviceInfo> = _deviceInfo

    private var _bleScannedInfo: MutableLiveData<IoTBleScanned> = MutableLiveData()
    private var _device: MutableLiveData<IoTDevice> = MutableLiveData()
    val bleScannedData: LiveData<IoTBleScanned> = _bleScannedInfo
    val deviceData: LiveData<IoTDevice> = _device

    private var _rfGatewayId: MutableLiveData<String> = MutableLiveData()
    private var _pairedRfDevice: MutableLiveData<IoTPairedRfDevice> = MutableLiveData()
    val rfGatewayIdData: LiveData<String> = _rfGatewayId
    val pairedRfDeviceData: LiveData<IoTPairedRfDevice> = _pairedRfDevice

    fun setDeviceInfoData(data: IoTDirectDeviceInfo) {
        _deviceInfo.value = data
    }

    fun setDeviceData(data: IoTDevice) {
        _device.value = data
    }

    fun setBleScannedData(data: IoTBleScanned) {
        _bleScannedInfo.value = data
    }

    fun setRfGatewayIdData(data: String) {
        _rfGatewayId.value = data
    }

    fun setPairedRfDeviceData(data: IoTPairedRfDevice) {
        _pairedRfDevice.value = data
    }
}