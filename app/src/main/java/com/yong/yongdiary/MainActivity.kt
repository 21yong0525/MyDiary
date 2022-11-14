package com.yong.yongdiary

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.ExifInterface
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayoutMediator
import com.yong.yongdiary.databinding.ActivityMainBinding
import com.yong.yongdiary.databinding.UsertabButtonBinding
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class MainActivity : AppCompatActivity() {
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var binding: ActivityMainBinding
    lateinit var oneFragment: OneFragment
    lateinit var twoFragment: TwoFragment
    lateinit var threeFragment: ThreeFragment
    lateinit var filePath: String
    lateinit var dialogBookmark: CustomDialogBookmark

    var imgcheck = false
    var bookmarkList = mutableListOf<DataVOBookmark>()
    var imageList = arrayListOf<Bitmap>()
    var albumList = mutableListOf<DataVOImg>()
    var customAdapterAlbum = CustomAdapterAlbum(albumList)
    var customAdapterBookmark = CustomAdapterBookmark(bookmarkList)
    var requestCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()){}
    var pictureType = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        toggle = ActionBarDrawerToggle(
            this,
            binding.drawerLayout,
            R.string.drawer_open,
            R.string.drawer_close
        )
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        toggle.syncState()

        val pageAdapter = PageAdapter(this)
        val title = mutableListOf<String>("쓰기", "홈", "옵션")

        oneFragment = OneFragment()
        twoFragment = TwoFragment()
        threeFragment = ThreeFragment()

        customAdapterBookmark = CustomAdapterBookmark(bookmarkList)

        pageAdapter.addFragment(oneFragment, title[0])
        pageAdapter.addFragment(twoFragment, title[1])
        pageAdapter.addFragment(threeFragment, title[2])

        binding.viewpager.adapter = pageAdapter
        TabLayoutMediator(binding.tablayout, binding.viewpager) { tab, position ->
            tab.customView = createTabView(title[position])
        }.attach()

        binding.navigaionview.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navi_user_info -> {
                    val dialogUserInfo = CustomDialogUserInfo(binding.root.context)
                    dialogUserInfo.binding.ivUserImg.setImageBitmap(binding.userImg.drawable.toBitmap())
                    dialogUserInfo.binding.tvUserNicname.text = "닉네임\n${binding.userNickname.text}"
                    dialogUserInfo.binding.tvUserEmail.text = "이메일\n${binding.userEmail.text}"
                    dialogUserInfo.binding.tvUserMemo.text = "${twoFragment.dataList.size}개"
                    dialogUserInfo.binding.tvUserImg.text = "${imageList.size}장"
                    dialogUserInfo.showDialog()
                }
                R.id.navi_user_img -> {
                    val dialogAlbum = CustomDialogAlbum(binding.root.context)
                    dialogAlbum.binding.recycler.adapter = customAdapterAlbum
                    dialogAlbum.binding.recycler.layoutManager = GridLayoutManager(this,2)
                    dialogAlbum.binding.recycler.addItemDecoration(MyDecoration(this))
                    dialogAlbum.showDialog()
                }
                R.id.navi_bookmark -> {
                    dialogBookmark = CustomDialogBookmark(binding.root.context)
                    dialogBookmark.binding.recycler.adapter = customAdapterBookmark
                    dialogBookmark.binding.recycler.layoutManager = LinearLayoutManager(this)
                    dialogBookmark.binding.recycler.addItemDecoration(MyDecoration(this))
                    dialogBookmark.showDialog()
                }
            }
            true
        }

        binding.viewpager.currentItem = 1
        binding.viewpager.currentItem = 0

        requestCameraLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                try {
                    val calculateRatio = calculateInSampleSize(
                        (when (pictureType){
                            1 -> {Uri.fromFile(File(filePath))}
                            else -> {it.data!!.data!!}
                        }),
                        resources.getDimensionPixelSize(R.dimen.imgSize),
                        resources.getDimensionPixelSize(R.dimen.imgSize)
                    )
                    val options = BitmapFactory.Options()
                    options.inSampleSize = calculateRatio

                    val bitmap =
                        when (pictureType){
                            1 -> {BitmapFactory.decodeFile(filePath, options)}
                            else -> {BitmapFactory.decodeStream(contentResolver.openInputStream(it.data!!.data!!),null, options)}
                        }
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        val orientation = getOrientationOfImage(
                            when (pictureType) {
                                1 -> {Uri.fromFile(File(filePath))}
                                else -> {it.data!!.data!!}
                            }).toFloat()
                        val newBitmap = getRotatedBitmap(bitmap,orientation)
                        newBitmap?.let {
                            when (pictureType) {
                                1,2 -> {
                                    imgcheck = true
                                    oneFragment.binding.ivUserimg.setImageBitmap(newBitmap)
                                }
                                3 -> {
                                    binding.userImg.setImageBitmap(newBitmap)
                                    threeFragment.binding.ft3UserImg.setImageBitmap(newBitmap)
                                }
                            }
                            imageList.add(newBitmap)
                            val dataVOImg = DataVOImg(newBitmap)
                            refreshRecyclerViewAddImg(dataVOImg)
                        }?: let {
                            Log.e("yongdiary", "camera error")
                        }
                    } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.N) {
                        bitmap?.let {
                            when (pictureType) {
                                1,2 -> {
                                    imgcheck = true
                                    oneFragment.binding.ivUserimg.setImageBitmap(bitmap)
                                }
                                3 -> {
                                    binding.userImg.setImageBitmap(bitmap)
                                    threeFragment.binding.ft3UserImg.setImageBitmap(bitmap)
                                }
                            }
                            imageList.add(bitmap)
                            val dataVOImg = DataVOImg(bitmap)
                            refreshRecyclerViewAddImg(dataVOImg)
                        } ?: let {
                            Log.e("yongdiary", "camera error")
                        }
                    }
                } catch (e : java.lang.Exception) {
                    Log.e("yongdiary", e.stackTraceToString())
                }
            } else {
                Log.e("yongdiary","camera error")
            }
        }
    }

    fun createTabView(title: String): View {
        val useTabBinding = UsertabButtonBinding.inflate(layoutInflater)
        useTabBinding.tvTabName.text = title
        when (title) {
            "쓰기" -> {
                useTabBinding.ivTabLogo.setImageResource(R.drawable.ic_diary)
            }
            "홈" -> {
                useTabBinding.ivTabLogo.setImageResource(R.drawable.ic_home)
            }
            "옵션" -> {
                useTabBinding.ivTabLogo.setImageResource(R.drawable.ic_settings)
            }
        }
        return useTabBinding.root
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)

        val menuItem = menu?.findItem(R.id.menu_search)
        val searchView = menuItem?.actionView as SearchView

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {

            override fun onQueryTextSubmit(searchText: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(searchText: String?): Boolean {
                for (i in 0 until twoFragment.dataList.size) {
                    if (twoFragment.dataList[i].inPutData.contains("$searchText")) {
                        binding.viewpager.currentItem = 1
                        twoFragment.binding.recyclerView.smoothScrollToPosition(i)
                        return true
                    }
                }
                return true
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)) {
            return true
        }
        when (item.itemId) {
            R.id.menu_save -> {
                Toast.makeText(applicationContext, "저장!", Toast.LENGTH_SHORT).show()
            }
            R.id.menu_load -> {
                Toast.makeText(applicationContext, "불러오기!", Toast.LENGTH_SHORT).show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    fun bringingPictures(type: Int) {
        when (type) {
            1 -> {
                val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
                val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
                val file = File.createTempFile("__jpg_${timeStamp}", ".jpg", storageDir)
                filePath = file.absolutePath
                val photoUri = FileProvider.getUriForFile(this, "com.yong.yongdiary.fileprovider", file)
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                requestCameraLauncher.launch(intent)
            }
            2 -> {
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                intent.type = "image/*"
                requestCameraLauncher.launch(intent)
            }
        }
    }

    fun calculateInSampleSize(fileUri: Uri, reqWidth: Int, reqHeight: Int): Int {
        val options = BitmapFactory.Options()
        options.inJustDecodeBounds = true
        try {
            var inputStream = contentResolver.openInputStream(fileUri)
            BitmapFactory.decodeStream(inputStream, null, options)
            inputStream = null
        } catch (e: java.lang.Exception) {
            Log.e("yongdiary", e.stackTraceToString())
        }
        val height = options.outHeight
        val width = options.outWidth
        var inSampleSize = 1

        if (height> reqHeight || width > reqWidth) {
            val halfHeight = height / 2
            val halfWidth = width / 2
            while (halfHeight/inSampleSize >= reqHeight && halfWidth/inSampleSize >= reqWidth) {
                inSampleSize *= 2
            }
        }
        return inSampleSize
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun getOrientationOfImage(uri: Uri): Int {
        val inputStream = contentResolver.openInputStream(uri)
        val exif: ExifInterface? = try {
            ExifInterface(inputStream!!)
        } catch (e: IOException) {
            e.printStackTrace()
            return -1
        }
        inputStream.close()

        val orientation = exif?.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL)
        if (orientation != -1) {
            when (orientation) {
                ExifInterface.ORIENTATION_ROTATE_90 -> return 90
                ExifInterface.ORIENTATION_ROTATE_180 -> return 180
                ExifInterface.ORIENTATION_ROTATE_270 -> return 270
            }
        }
        return 0
    }

    @Throws(Exception::class)
    private fun getRotatedBitmap(bitmap: Bitmap?, degrees: Float): Bitmap? {
        if (bitmap == null) return null
        if (degrees == 0F) return bitmap
        val m = Matrix()
        m.setRotate(degrees, bitmap.width.toFloat() / 2, bitmap.height.toFloat() / 2)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, m, true)
    }

    fun searchMemo(inputTime: String) {
        for (i in 0 until twoFragment.dataList.size) {
            if (twoFragment.dataList[i].inputTime.contains("$inputTime")) {
                dialogBookmark.close()
                binding.viewpager.currentItem = 1
                twoFragment.binding.recyclerView.smoothScrollToPosition(i)
                Toast.makeText(applicationContext, "해당 메모로 이동합니다.", Toast.LENGTH_SHORT).show()
                val dataVO = twoFragment.dataList[i]
                openMemo(dataVO)
                binding.drawerLayout.close()
            }
        }
    }

    fun refreshRecyclerViewAddImg(dataVOImg: DataVOImg) {
        albumList.add(dataVOImg)
        customAdapterAlbum.notifyDataSetChanged()
    }

    fun refreshRecyclerViewAddBookmark(dataVOBookmark: DataVOBookmark) {
        bookmarkList.add(dataVOBookmark)
        customAdapterBookmark.notifyDataSetChanged()
    }

    fun refreshRecyclerViewDropBookmark(dataVOBookmark: DataVOBookmark, inputTime: String) {
        for (i in 0 until twoFragment.dataList.size) {
            if (twoFragment.dataList[i].inputTime.contains("$inputTime")) {
                dialogBookmark.close()
                twoFragment.dataList[i].bookmark = "☆"
                twoFragment.customAdapter.notifyDataSetChanged()
                binding.drawerLayout.close()
            }
        }
        bookmarkList.remove(dataVOBookmark)
        customAdapterBookmark.notifyDataSetChanged()
    }


    fun openMemo(dataVO: DataVO) {
        val dialogMemo = CustomDialogMemo(binding.root.context)
        dialogMemo.binding.tvDate.text = dataVO.date
        dialogMemo.binding.tvInput.text = dataVO.inPutData
        dialogMemo.binding.checkBox1.text = dataVO.check1Name
        dialogMemo.binding.checkBox2.text = dataVO.check2Name
        dialogMemo.binding.checkBox3.text = dataVO.check3Name
        if (dataVO.check1 == true) {
            dialogMemo.binding.tvFtMsg.visibility = View.VISIBLE
            dialogMemo.binding.checkBox1.visibility = View.VISIBLE
        }
        if (dataVO.check2 == true) {
            dialogMemo.binding.checkBox2.visibility = View.VISIBLE
        }
        if (dataVO.check3 == true) {
            dialogMemo.binding.checkBox3.visibility = View.VISIBLE
        }
        if (dataVO.imgCheck == true) {
            dialogMemo.binding.ivUserimg.setImageBitmap(dataVO.img)
            dialogMemo.binding.ivUserimg.visibility = View.VISIBLE
        }
        dialogMemo.showDialog()
    }
}
