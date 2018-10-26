package io.github.droidkaigi.confsched2019.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import dagger.Module
import dagger.Provides
import dagger.android.ContributesAndroidInjector
import dagger.android.support.DaggerFragment
import io.github.droidkaigi.confsched2019.R
import io.github.droidkaigi.confsched2019.databinding.FragmentMainBinding
import io.github.droidkaigi.confsched2019.session.ui.AllSessionsFragment
import io.github.droidkaigi.confsched2019.session.ui.AllSessionsFragmentModule
import io.github.droidkaigi.confsched2019.session.ui.DaySessionsFragment
import io.github.droidkaigi.confsched2019.session.ui.DaySessionsFragmentModule
import io.github.droidkaigi.confsched2019.session.ui.FavoriteSessionsFragment
import io.github.droidkaigi.confsched2019.session.ui.FavoriteSessionsFragmentModule
import io.github.droidkaigi.confsched2019.user.actioncreator.UserActionCreator
import io.github.droidkaigi.confsched2019.user.store.UserStore
import javax.inject.Inject

class MainFragment : DaggerFragment() {
    lateinit var binding: FragmentMainBinding
    @Inject
    lateinit var userActionCreator: UserActionCreator
    @Inject
    lateinit var userStore: UserStore

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_main,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (savedInstanceState == null) {
            val navHostFragment = childFragmentManager
                .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
            NavigationUI.setupWithNavController(
                binding.bottomNavView,
                navHostFragment.navController
            )
        }
    }

    override fun onStart() {
        super.onStart()
        userActionCreator.setupUser()
    }
}

@Module
abstract class MainFragmentModule {

    @ContributesAndroidInjector(modules = [AllSessionsFragmentModule::class])
    abstract fun contributeAllSessionsFragment(): AllSessionsFragment

    @ContributesAndroidInjector(modules = [DaySessionsFragmentModule::class])
    abstract fun contributeDaySessionsFragment(): DaySessionsFragment

    @ContributesAndroidInjector(modules = [FavoriteSessionsFragmentModule::class])
    abstract fun contributeFavoriteSessionsFragment(): FavoriteSessionsFragment

    @Module
    companion object {

        @Provides @JvmStatic fun providesActivity(mainFragment: MainFragment): FragmentActivity {
            return mainFragment.requireActivity()
        }

        @Provides @JvmStatic fun providesLifecycle(mainFragment: MainFragment): LifecycleOwner {
            return mainFragment.viewLifecycleOwner
        }
    }

}