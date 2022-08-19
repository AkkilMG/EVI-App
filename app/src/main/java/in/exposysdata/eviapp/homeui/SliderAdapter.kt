package `in`.exposysdata.eviapp.homeui


import `in`.exposysdata.eviapp.R
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.bumptech.glide.request.RequestOptions


class SliderAdapter(private val context: Context, private val imagelist: List<String>) :
    RecyclerView.Adapter<SliderAdapter.MyViewHolder>() {


    inner class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var imageview: ImageView = view.findViewById(R.id.cardimageview)

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.horizontal_item, parent, false)
        return MyViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {


        val options = RequestOptions()
            .diskCacheStrategy(DiskCacheStrategy.ALL)

        Glide.with(context)
            .load(imagelist[position])
            .transition(withCrossFade())
            .apply(options)
            .into(holder.imageview)
    }

    override fun getItemCount(): Int {
        return imagelist.size
    }


}