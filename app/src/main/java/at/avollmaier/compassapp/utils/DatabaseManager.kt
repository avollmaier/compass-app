import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.widget.Toast
import at.avollmaier.compassapp.R

class DatabaseManager {
    private var context: Context
    private val sqlDb: SQLiteDatabase

    constructor(ctx: Context) {
        this.context = ctx
        val db = DatabaseHelperWaypoints(this.context)
        sqlDb = db.writableDatabase
    }


    companion object {
        private const val DATABASE_VERSION = 1
        private const val DATABASE_NAME = "WaypointsDatabase.db"
        private const val TABLE_NAME = "waypoints"
        private const val COLUMN_ID = "id"
        const val COLUMN_LABEL = "label"
        const val COLUMN_LATITUDE = "latitude"
        const val COLUMN_LONGITUDE = "longitude"
    }

    val createTable =
        "CREATE TABLE $TABLE_NAME ($COLUMN_ID INTEGER PRIMARY KEY AUTOINCREMENT, $COLUMN_LABEL TEXT, $COLUMN_LATITUDE REAL, $COLUMN_LONGITUDE REAL)"

    inner class DatabaseHelperWaypoints(context: Context) :
        SQLiteOpenHelper(context, DATABASE_NAME, null, DATABASE_VERSION) {
        var context: Context? = context


        override fun onCreate(db: SQLiteDatabase) {
            db.execSQL(createTable)
            Toast.makeText(
                this.context,
                this.context?.resources?.getString(R.string.DB_Created) ?: "DB Created",
                Toast.LENGTH_SHORT
            ).show()
        }


        override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
            db.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
            onCreate(db)
            Toast.makeText(
                this.context,
                this.context?.resources?.getString(R.string.DB_Upgraded) ?: "DB Upgraded",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    fun insertWaypoint(label: String, latitude: Double, longitude: Double): Long {
        val contentValues = ContentValues()
        contentValues.put(COLUMN_LABEL, label)
        contentValues.put(COLUMN_LATITUDE, latitude)
        contentValues.put(COLUMN_LONGITUDE, longitude)
        return sqlDb.insert(TABLE_NAME, null, contentValues)
    }

    fun getAllWaypoints(): Cursor {
        return sqlDb.rawQuery("SELECT * FROM $TABLE_NAME", null)
    }

    fun deleteAllData() {
        sqlDb.execSQL("DELETE FROM $TABLE_NAME")
        Toast.makeText(
            this.context,
            this.context.resources.getString(R.string.DB_Cleared),
            Toast.LENGTH_SHORT
        ).show()
    }
}
