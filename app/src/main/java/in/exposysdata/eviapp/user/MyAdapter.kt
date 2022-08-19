package `in`.exposysdata.eviapp.user

import `in`.exposysdata.eviapp.R
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val userList : ArrayList<UserData>) : RecyclerView.Adapter<MyAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.user_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]
        println("Current Item: $currentitem")
        holder.fullName.text = currentitem.fullName
        holder.email.text = currentitem.email
        holder.phoneNumber.text = currentitem.phoneNumber
    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val fullName : TextView = itemView.findViewById(R.id.fullName)
        val email : TextView = itemView.findViewById(R.id.email)
        val phoneNumber : TextView = itemView.findViewById(R.id.phoneNumber)
    }

}

