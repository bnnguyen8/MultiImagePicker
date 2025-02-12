package ca.bnnguyen.multiimagepicker

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.annotation.VisibleForTesting
import androidx.fragment.app.Fragment
import ca.bnnguyen.multiimagepicker.Constants.Companion.BUNDLE_IMAGE_PICKED_SUCCESS
import ca.bnnguyen.multiimagepicker.Constants.Companion.BUNDLE_MAX_SELECTION_LIMIT
import ca.bnnguyen.multiimagepicker.Constants.Companion.BUNDLE_SELECTED_IMAGE_RESULT
import ca.bnnguyen.multiimagepicker.Constants.Companion.BUNDLE_SHOW_ALBUMS


class MultiImagePicker {
    companion object{
        const val REQUEST_PICK_MULTI_IMAGES = 369

        fun with(activity: Activity): Builder{
            return Builder(activity)
        }

        fun with(fragment: Fragment): Builder {
            return Builder(fragment)
        }
    }

    class Builder private constructor(){

        private var activity: Activity? = null
        private var fragment: Fragment? = null

        private var useActivity: Boolean = true

        private var showAlbums: Boolean = false

        private var maxSelectionLimit: Int = 1

        internal constructor(activity: Activity) : this() {
            this.activity = activity
            this.useActivity = true
        }

        internal constructor(fragment: Fragment) : this() {
            this.fragment = fragment
            this.useActivity = false
        }

        /**
         * If you will not provide or will provide more than 30, it will set max limit to 30
         */
        fun setSelectionLimit(maxLimit: Int): Builder{
            maxSelectionLimit = maxLimit
            return this
        }

        /**
         * This functionality is in progress so true/false will not work, and default is false
         */
        @VisibleForTesting
        fun showAlbum(show: Boolean = false): Builder{
            this.showAlbums = show
            return this
        }

        fun open(){
            val activity = if(useActivity) activity else fragment?.activity

            activity?.let {
                val intent = Intent(activity, StartImagePickerActivity::class.java)
                intent.putExtra(BUNDLE_MAX_SELECTION_LIMIT, maxSelectionLimit)
                intent.putExtra(BUNDLE_SHOW_ALBUMS, showAlbums)
                activity.startActivityForResult(intent, REQUEST_PICK_MULTI_IMAGES)
            } ?: kotlin.run {
                throw Exception("Activity/Fragment is not defined to start MultiImagePicker!")
            }
        }
    }

    class Result(private val data: Intent?){

        fun isSuccess(): Boolean {
            return data != null && data.hasExtra(BUNDLE_IMAGE_PICKED_SUCCESS) && data.getBooleanExtra(
                BUNDLE_IMAGE_PICKED_SUCCESS,false)
        }

        fun getImageList(): ArrayList<Uri>{
            if(isSuccess()) {
                return data?.getParcelableArrayListExtra(BUNDLE_SELECTED_IMAGE_RESULT)
                    ?: arrayListOf()
            }
            return arrayListOf()
        }

        fun getImageListAsAbsolutePath(context: Context): ArrayList<String> {
            val imageUriList = getImageList()
            val imageAbsPathList = arrayListOf<String>()

            imageUriList.forEach {
                imageAbsPathList.add(FileUtils.getPath(context,it))
            }

            return imageAbsPathList
        }

    }
}