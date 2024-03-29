import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import at.avollmaier.compassapp.MapsActivity
import at.avollmaier.compassapp.R
import at.avollmaier.compassapp.models.Waypoint

class WaypointAdapter(var waypoints: List<Waypoint>) :
    RecyclerView.Adapter<WaypointAdapter.WaypointViewHolder>() {

    class WaypointViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val labelTextView: TextView = itemView.findViewById(R.id.tvLabel)
        val latitudeTextView: TextView = itemView.findViewById(R.id.tvLatitude)
        val longitudeTextView: TextView = itemView.findViewById(R.id.tvLongitude)
        val mainLayout: LinearLayout = itemView.findViewById(R.id.lt_main)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WaypointViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_waypoint, parent, false)
        return WaypointViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: WaypointViewHolder, position: Int) {
        val waypoint = waypoints[position]

        holder.labelTextView.text = waypoint.label
        holder.latitudeTextView.text = "Latitude: ${waypoint.latitude}"
        holder.longitudeTextView.text = "Longitude: ${waypoint.longitude}"
        holder.mainLayout.setOnClickListener {
            val intent = Intent(holder.itemView.context, MapsActivity::class.java)
            intent.putExtra("waypoint", waypoint.toString())
            holder.itemView.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return waypoints.size
    }
}
