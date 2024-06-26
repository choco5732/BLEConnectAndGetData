package com.example.protocol20datainfo.presentation

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.animation.AnticipateInterpolator
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.graphics.Color
import androidx.core.animation.doOnEnd
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.protocol20datainfo.R
import com.example.protocol20datainfo.databinding.MainActivityBinding
import com.example.protocol20datainfo.presentation.adapter.MainViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

@SuppressLint("MissingPermission")
class MainActivity : AppCompatActivity() {

    companion object {
        /**
         * Service UUID: e093f3b5-00a3-a9e5-9eca-40016e0edc24
         *                  * Characteristic UUID: e093f3b5-00a3-a9e5-9eca-40026e0edc24
         *                  * Characteristic UUID: e093f3b5-00a3-a9e5-9eca-40036e0edc24
         */
        const val serviceUuidT10 = "e093f3b5-00a3-a9e5-9eca-40016e0edc24"
        const val characteristicUuidReadT10 = "e093f3b5-00a3-a9e5-9eca-40026e0edc24"
        const val characteristicUuidWriteT10 = "e093f3b5-00a3-a9e5-9eca-40036e0edc24"

        const val serviceUuidT21 = "e093f3b5-00a3-a9e5-9eca-40016e0edc24"
        const val characteristicUuidReadT21 = "e093f3b5-00a3-a9e5-9eca-40026e0edc24"
        const val characteristicUuidWriteT21 = "e093f3b5-00a3-a9e5-9eca-40036e0edc24";

        private const val UUID_CONNECTION_SERVICE_T01 = "e1b40000-ffc4-4daa-a49b-1c92f99072ab"
        private const val UUID_CONNECTION_CHARACTERISTIC_WRITE_T01 =
            "e1b40002-ffc4-4daa-a49b-1c92f99072ab"
        private const val UUID_CONNECTION_CHARACTERISTIC_READ_T01 =
            "e1b40001-ffc4-4daa-a49b-1c92f99072ab"

        var state: Boolean = false
    }

    lateinit var binding: MainActivityBinding
    private val viewPagerAdapter by lazy {
        MainViewPagerAdapter(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen() // 꼭 binding.root 위에 있어야 한다. 명심!
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() = with(binding) {
        viewPager.adapter = viewPagerAdapter
        viewPager.isUserInputEnabled = false // 옆으로 스크롤해서 탭 이동하는걸 끄는 기능
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.setIcon(viewPagerAdapter.getIcon(position))
            tab.setText(viewPagerAdapter.getTitle(position))
        }.attach()

        for (i in 0 .. tabLayout.tabCount) {
            val params = tabLayout.getTabAt(i)?.view?.getChildAt(0)?.layoutParams as LinearLayout.LayoutParams?
            params?.bottomMargin = 0
            tabLayout.getTabAt(i)?.view?.getChildAt(0)?.layoutParams = params
        }

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            @SuppressLint("ResourceAsColor")
            override fun onTabSelected(tab: TabLayout.Tab?) {
                changeTabIconColorPressed(tab)
                reduceTabLayoutMargin()
            }

            override fun onTabUnselected(tab: TabLayout.Tab?) {
                changeTabIconColorUnpressed(tab)
                reduceTabLayoutMargin()
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        // 스플래쉬 API 애니메이션 설정
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            splashScreen.setOnExitAnimationListener { splashScreenView ->
                // Create your custom animation.
                val slideUp = ObjectAnimator.ofFloat(
                    splashScreenView,
                    View.TRANSLATION_Y,
                    0f,
                    -splashScreenView.height.toFloat()
                )
                slideUp.interpolator = AnticipateInterpolator()
                slideUp.duration = 600L

                // Call SplashScreenView.remove at the end of your custom animation.
                slideUp.doOnEnd { splashScreenView.remove() }

                // Run your animation.
                slideUp.start()
            }
        }
    }

    private fun changeTabIconColorUnpressed(tab: TabLayout.Tab?) {
        val position = tab?.position
        when (position) {
            0 -> {
                tab.setIcon(R.drawable.ic_list_unpressed)
            }
            1 -> {
                tab.setIcon(R.drawable.ic_receive_unpressed)
            }
        }
    }

    private fun changeTabIconColorPressed(tab: TabLayout.Tab?) {
        val position = tab?.position
        when (position) {
            0 -> {
                tab.setIcon(R.drawable.ic_list_pressed)
            }
            1 -> {
                tab.setIcon(R.drawable.ic_receive_pressed)
            }
        }
    }

    private fun reduceTabLayoutMargin() {
        for (i in 0 .. binding.tabLayout.tabCount) {
            val params = binding.tabLayout.getTabAt(i)?.view?.getChildAt(0)?.layoutParams as LinearLayout.LayoutParams?
            params?.bottomMargin = 0
            binding.tabLayout.getTabAt(i)?.view?.getChildAt(0)?.layoutParams = params
        }
    }
}