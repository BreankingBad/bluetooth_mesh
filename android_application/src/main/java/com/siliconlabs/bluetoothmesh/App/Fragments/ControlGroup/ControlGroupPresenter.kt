/*
 * Copyright © 2019 Silicon Labs, http://www.silabs.com. All rights reserved.
 */

package com.siliconlabs.bluetoothmesh.App.Fragments.ControlGroup

import android.bluetooth.BluetoothAdapter
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.util.Log
import android.view.View
import com.siliconlab.bluetoothmesh.adk.ErrorType
import com.siliconlab.bluetoothmesh.adk.data_model.subnet.Subnet
import com.siliconlabs.bluetoothmesh.App.BasePresenter
import com.siliconlabs.bluetoothmesh.App.Fragments.DeviceList.DeviceListAdapter
import com.siliconlabs.bluetoothmesh.App.Fragments.DeviceList.SCAN_PERIOD
import com.siliconlabs.bluetoothmesh.App.Fragments.Network.NetworkView
import com.siliconlabs.bluetoothmesh.App.Logic.MeshLogic
import com.siliconlabs.bluetoothmesh.App.Logic.NetworkConnectionListener
import com.siliconlabs.bluetoothmesh.App.Logic.NetworkConnectionLogic
import com.siliconlabs.bluetoothmesh.App.ModelView.MeshNode
import com.siliconlabs.bluetoothmesh.App.Models.DeviceFunctionality
import com.siliconlabs.bluetoothmesh.App.Models.MeshElementControl
import com.siliconlabs.bluetoothmesh.App.Models.MeshGroupControl
import com.siliconlabs.bluetoothmesh.App.Models.MeshNodeManager
import com.siliconlabs.bluetoothmesh.App.statusOfNode
import java.util.*
import kotlin.concurrent.schedule

/**
 * @author Comarch S.A.
 */

class ControlGroupPresenter(private val controlGroupView: ControlGroupView, val networkConnectionLogic: NetworkConnectionLogic, val meshLogic: MeshLogic, private val meshGroupControl: MeshGroupControl, val meshNodeManager: MeshNodeManager) : BasePresenter, NetworkConnectionListener, DeviceListAdapter.DeviceItemListener, MeshGroupControl.SetCallback, MeshElementControl.SetCallback {
    private val TAG: String = javaClass.canonicalName!!

    val groupInfo = meshLogic.currentGroup!!
    val networkInfo: Subnet = meshLogic.currentSubnet!!
    var statusOfNodes : ArrayList<statusOfNode> = ArrayList()

    var nodes: Set<MeshNode> = emptySet()

    override fun onResume() {
        networkConnectionLogic.addListener(this)
        controlGroupView.setMasterControlEnabled(false)
        controlGroupView.setMasterControlVisibility(View.GONE)
        refreshList()
    }

    override fun onPause() {
        networkConnectionLogic.removeListener(this)
    }

    fun refreshList() {
        nodes = meshNodeManager.getMeshNodes(groupInfo)
        controlGroupView.setDevicesList(nodes)
        if (nodes.isEmpty()) {
            controlGroupView.setMasterControlVisibility(View.GONE)
        } else {
            controlGroupView.setMasterControlVisibility(View.VISIBLE)
        }
    }

    private fun changeDevicesState(level: Int) {
        nodes.forEach { device ->
            onSeekBarChangeListener(device, level, device.temperaturePercentage, device.deltaUvPercentage)
            if (level > 0) {
                controlGroupView.setMasterSwitch(true)
            } else {
                controlGroupView.setMasterSwitch(false)
            }

            controlGroupView.setMasterLevel(level)
        }
    }

    // NetworkConnectionListener

    override fun connecting() {
        controlGroupView.setMeshIconState(ControlGroupView.MESH_ICON_STATE.CONNECTING)
        refreshList()
    }

    override fun connected() {
        controlGroupView.setMeshIconState(ControlGroupView.MESH_ICON_STATE.CONNECTED)
        controlGroupView.setMasterControlEnabled(true)
        refreshList()
    }

    override fun disconnected() {
        controlGroupView.setMeshIconState(ControlGroupView.MESH_ICON_STATE.DISCONNECTED)
        controlGroupView.setMasterControlEnabled(false)
        refreshList()
    }

    override fun connectionMessage(message: NetworkConnectionListener.MESSAGE) {
        controlGroupView.showErrorToast(message.toString())
    }

    override fun connectionErrorMessage(error: ErrorType) {
        controlGroupView.showErrorToast(error)
    }

    // View callbacks

    fun meshIconClicked(iconState: ControlGroupView.MESH_ICON_STATE) {
        when (iconState) {
            ControlGroupView.MESH_ICON_STATE.DISCONNECTED -> {
                networkConnectionLogic.connect(networkInfo)
            }
            ControlGroupView.MESH_ICON_STATE.CONNECTING -> {
                networkConnectionLogic.disconnect()
            }
            ControlGroupView.MESH_ICON_STATE.CONNECTED -> {
                networkConnectionLogic.disconnect()
            }
        }
    }

    fun onMasterSwitchChanged(isChecked: Boolean) {
        Log.d(TAG, "MasterSwitchChanged isChecked:$isChecked")

        val level = if (isChecked) 100 else 0

        onMasterLevelChanged(level)
    }

    fun onMasterLevelChanged(percentage: Int) {
        Log.d(TAG, "MasterLeverChanged levelPercentage:$percentage")

        val onOffExist = nodes.any { it.functionality == DeviceFunctionality.FUNCTIONALITY.OnOff }
        val levelExist = nodes.any { it.functionality == DeviceFunctionality.FUNCTIONALITY.Level }
        val lightnessExist = nodes.any { it.functionality == DeviceFunctionality.FUNCTIONALITY.Lightness }

        if (onOffExist) {
            meshGroupControl.setOnOff(percentage > 0, this)
        }
        if (levelExist) {
            meshGroupControl.setLevel(percentage, this)
        }
        if (lightnessExist) {
            meshGroupControl.setLightness(percentage, this)
        }

        nodes.forEach {
            it.onOffState = percentage > 0
            it.levelPercentage = percentage
            it.lightnessPercentage = percentage
        }

        changeDevicesState(percentage)
        controlGroupView.refreshView()
    }

    // Adapter callbacks

    override fun onClickDeviceImageListener(deviceInfo: MeshNode) {
        val nodeElementControl = MeshElementControl(deviceInfo.node.elements[0], groupInfo)

        when (deviceInfo.functionality) {
            DeviceFunctionality.FUNCTIONALITY.OnOff -> {
                val newOnOffState = !deviceInfo.onOffState

                nodeElementControl.setOnOff(newOnOffState, this)
                deviceInfo.onOffState = newOnOffState
            }
            DeviceFunctionality.FUNCTIONALITY.Level -> {
                var newLevelPercentage = 100
                if (deviceInfo.levelPercentage > 0) {
                    newLevelPercentage = 0
                }

                nodeElementControl.setLevel(newLevelPercentage, this)
                deviceInfo.levelPercentage = newLevelPercentage
            }
            DeviceFunctionality.FUNCTIONALITY.Lightness -> {
                var newLightnessPercentage = 100
                if (deviceInfo.lightnessPercentage > 0) {
                    newLightnessPercentage = 0
                }

                nodeElementControl.setLightness(newLightnessPercentage, this)
                deviceInfo.lightnessPercentage = newLightnessPercentage
            }
            DeviceFunctionality.FUNCTIONALITY.CTL -> {
                var newLightnessPercentage = 100
                if (deviceInfo.lightnessPercentage > 0) {
                    newLightnessPercentage = 0
                }

                nodeElementControl.setColorTemperature(newLightnessPercentage, deviceInfo.temperaturePercentage, deviceInfo.deltaUvPercentage, this)
                deviceInfo.lightnessPercentage = newLightnessPercentage
            }
        }

        controlGroupView.refreshView()
    }

    override fun onSeekBarChangeListener(deviceInfo: MeshNode, levelPercentage: Int, temperaturePercentage: Int?, deltaUvPercentage: Int?) {
        val nodeElementControl = MeshElementControl(deviceInfo.node.elements[0], groupInfo)

        when (deviceInfo.functionality) {
            DeviceFunctionality.FUNCTIONALITY.Level -> {
                nodeElementControl.setLevel(levelPercentage, this)

                deviceInfo.levelPercentage = levelPercentage
            }
            DeviceFunctionality.FUNCTIONALITY.Lightness -> {
                nodeElementControl.setLightness(levelPercentage, this)

                deviceInfo.lightnessPercentage = levelPercentage
            }
            DeviceFunctionality.FUNCTIONALITY.CTL -> {
                if (temperaturePercentage != null && deltaUvPercentage != null) {
                    nodeElementControl.setColorTemperature(levelPercentage, temperaturePercentage, deltaUvPercentage, this)

                    deviceInfo.lightnessPercentage = levelPercentage
                    deviceInfo.temperaturePercentage = temperaturePercentage
                    deviceInfo.deltaUvPercentage = deltaUvPercentage
                }
            }
        }

        controlGroupView.refreshView()
    }


    //group control callback

    override fun error(error: ErrorType) {
        controlGroupView.showErrorToast(error)
    }

    //adapter callback

    override fun onDeleteClickListener(deviceInfo: List<MeshNode>) {
        controlGroupView.showDeleteDeviceDialog(deviceInfo)
    }

    override fun onConfigClickListener(deviceInfo: MeshNode) {
        controlGroupView.showDeviceConfigDialog(deviceInfo)
    }

    private val bleScanner = object : ScanCallback() {
        override fun onScanResult(callbackType: Int, result: ScanResult?) {

            Log.e("SCAN","MAC:${result?.device?.address} - Manu:${result?.scanRecord?.manufacturerSpecificData}")
            Log.e("RAW DATA","SIZE:${result?.scanRecord?.getManufacturerSpecificData(767)?.size}- DATA:${result?.scanRecord?.getManufacturerSpecificData(767)}")

            val rawData = result?.scanRecord?.getManufacturerSpecificData(767)

            if(rawData == null){
                Log.e("DEBUG","CAN'T FIND DEVICE")
            }
            else{
                //Change from bytearray to byte
                var data : ArrayList<Byte> = ArrayList()

                rawData?.forEach {
                    data.add(it)
                }
                Log.e("QQQQQ",data.toString())
                statusOfNodes = checkStatusNode(data)

                Log.d("DATA FINAL",statusOfNodes.toString())
            }
        }
    }


    private val bluetoothLeScanner: BluetoothLeScanner
        get() {
            val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
            return bluetoothAdapter.bluetoothLeScanner
        }

    private fun checkStatusNode( a : ArrayList<Byte>) : ArrayList<statusOfNode>{
        var index = 0
        var stt = 1
        var indexMax = a.size - 2
        var statusOfNodes : ArrayList<statusOfNode> = ArrayList()
        while(index <= indexMax){

            var heartBeat = 0x01 and a[index].toInt()
            var preBattery = a[index].toInt() shr 1
            var Battery = 0x7F and preBattery
            println("Node $stt : Heartbeat = $heartBeat || Battery = $Battery")


            var alarmSignal = 0x03 and a[index + 1].toInt()
            var preAddress = a[index + 1].toInt() shr 2
            var Address = 0x3F and preAddress
            println("Node $stt : Alarm Signal = $alarmSignal || Unicast Address = $Address")
            println("**************************************************")

            statusOfNodes.add(statusOfNode(heartBeat,Battery,alarmSignal,Address))

            stt += 1
            index += 2
        }
        return statusOfNodes
    }

    fun scanAdvertiseBle(){

        println("**********Start scan************")
        bluetoothLeScanner.startScan(bleScanner)

        Timer().schedule(SCAN_PERIOD) {
            println("***********Stop scan***********")
            bluetoothLeScanner.stopScan(bleScanner)
        }
    }

    fun onChangeStatusGroup(){
        scanAdvertiseBle()

        nodes.forEach {
            for(index in statusOfNodes.indices){
                if(it.node.primaryElementAddress == statusOfNodes[index].unicastAddress){
                    it.heartBeat = statusOfNodes[index].heartBeat
                    it.battery = statusOfNodes[index].battery
                    it.alarmSignal = statusOfNodes[index].alarmSignal
                }
            }
        }
    }
}