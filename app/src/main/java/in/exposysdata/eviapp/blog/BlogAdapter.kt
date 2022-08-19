package `in`.exposysdata.eviapp.blog

import `in`.exposysdata.eviapp.R
import android.graphics.BitmapFactory
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import java.net.URL


class BlogAdapter(private val blogList : ArrayList<BlogHomeData>) : RecyclerView.Adapter<BlogAdapter.BlogViewHolder>() {

    class BlogViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val title: TextView = itemView.findViewById(R.id.blog_title)
        val description: TextView = itemView.findViewById(R.id.blog_description)
        val image: ImageView = itemView.findViewById(R.id.blog_image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BlogViewHolder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: BlogViewHolder, position: Int) {
        val currentItem = blogList[position]
        // holder.fullName.text = currentItem.fullName
        holder.title.text = currentItem.title
        holder.description.text = currentItem.description
        val newurl = URL(currentItem.image)
        var mIcon_val = BitmapFactory.decodeStream(newurl.openConnection().getInputStream())
        holder.image.setImageBitmap(mIcon_val)

    }

    override fun getItemCount(): Int {
        return blogList.size
    }

}