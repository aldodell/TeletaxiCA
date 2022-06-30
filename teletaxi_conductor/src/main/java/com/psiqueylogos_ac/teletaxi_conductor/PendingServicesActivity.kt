package com.psiqueylogos_ac.teletaxi_conductor

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.DocumentChange
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

import com.psiqueylogos_ac.teletaxi_lib.Order
import com.psiqueylogos_ac.teletaxi_lib.Settings
import com.psiqueylogos_ac.teletaxi_lib.StatusOrder

/**
 * This class manage pending services queue. Driver can take a service
 */
class PendingServicesAdapter(
    private var orders: List<Order>,
    private var db: FirebaseFirestore,
    private var settings: Settings
) :
    RecyclerView.Adapter<PendingServicesAdapter.ViewHolder>() {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvServiceDescription: TextView = itemView.findViewById(R.id.tvServiceDescription)
        val btAcceptService: Button = itemView.findViewById(R.id.btAcceptService)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.service_row, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val order = orders[position]
        val t = "Desde: ${order.origin} hasta: ${order.destination} costo: ${order.cost}"
        holder.tvServiceDescription.setText(t)
        holder.btAcceptService.setOnClickListener {

            AlertDialog.Builder(it.context)
                .setTitle(R.string.service_accept)
                .setMessage(R.string.service_message_1)
                .setPositiveButton(
                    R.string.accept
                ) { _, _ ->

                    order.status = StatusOrder.accepted.name
                    order.driver = settings.email
                    settings.idOrder = order.id

                    db.collection("orders")
                        .document(order.id)
                        .update(order.map)
                        .addOnCompleteListener {
                            val mIntent =
                                Intent(settings.context, CurrentServiceActivity::class.java)
                            settings.context.startActivity(mIntent)
                        }
                }
                .setNegativeButton(R.string.close) { d, _ -> d.dismiss() }
                .show()
        }
    }

    override fun getItemCount(): Int {
        return orders.size

    }
}


class PendingServicesActivity : AppCompatActivity() {

    private lateinit var rvPendingServices: RecyclerView
    private var pendingOrders = ArrayList<Order>()
    private lateinit var pendingServicesAdapter: PendingServicesAdapter
    private val db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending_services)
        rvPendingServices = findViewById(R.id.rvPendingServices)
        pendingServicesAdapter = PendingServicesAdapter(pendingOrders, db, Settings(this))
        rvPendingServices.adapter = pendingServicesAdapter
        rvPendingServices.layoutManager = LinearLayoutManager(this)

        db.collection("orders")
            .addSnapshotListener { value, _ ->
                value?.let {
                    it.documentChanges.forEach { doc ->
                        if (doc.type == DocumentChange.Type.ADDED) {
                            val order = Order()
                            order.from(doc.document.data, doc.document.id)
                            if (order.status == StatusOrder.pending.name) {
                                pendingOrders.add(order)
                            }
                        } else if (doc.type == DocumentChange.Type.MODIFIED) {
                            val order = Order()
                            order.from(doc.document.data, doc.document.id)
                            if (order.status == StatusOrder.accepted.name) {
                                pendingOrders.remove(pendingOrders.first { order1 -> order1.id == order.id })
                            }
                        }
                    }
                }
                pendingServicesAdapter.notifyDataSetChanged()
            }
    }

}