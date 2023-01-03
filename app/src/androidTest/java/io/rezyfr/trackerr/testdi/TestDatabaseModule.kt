

package io.rezyfr.trackerr.testdi

import dagger.Binds
import dagger.Module
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.rezyfr.trackerr.core.data.TransactionRepository
import io.rezyfr.trackerr.core.data.di.DataModule
import io.rezyfr.trackerr.core.data.di.FakeTransactionRepository

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [DataModule::class]
)
interface FakeDataModule {

//    @Binds
//    abstract fun bindRepository(
//        fakeRepository: FakeTransactionRepository
//    ): TransactionRepository
}
