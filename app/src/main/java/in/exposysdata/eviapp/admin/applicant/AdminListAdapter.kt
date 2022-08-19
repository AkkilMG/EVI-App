package `in`.exposysdata.eviapp.admin.applicant

import `in`.exposysdata.eviapp.R
import `in`.exposysdata.eviapp.user.ApplicantData
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView



class AdminListAdapter(private val userList : ArrayList<ApplicantData>) : RecyclerView.Adapter<AdminListAdapter.MyViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {

        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.aplicant_list, parent, false)
        return MyViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        val currentitem = userList[position]
        println("Current Item: $currentitem")
        holder.applicantListFullName.text = currentitem.fullName
        holder.applicantPhoneNumber.text = currentitem.email
        holder.applicantEmail.text = currentitem.phoneNumber
        holder.applicantDate.text = currentitem.date

    }

    override fun getItemCount(): Int {
        return userList.size
    }


    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val applicantListFullName : TextView = itemView.findViewById(R.id.applicantListFullName)
        val applicantPhoneNumber : TextView = itemView.findViewById(R.id.applicantPhoneNumber)
        val applicantEmail : TextView = itemView.findViewById(R.id.applicantEmail)
        val applicantDate : TextView = itemView.findViewById(R.id.applicantDate)
    }

}

