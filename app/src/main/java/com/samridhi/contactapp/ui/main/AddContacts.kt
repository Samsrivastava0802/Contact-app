import android.content.ContentProviderOperation
import android.content.ContentResolver
import android.content.OperationApplicationException
import android.os.RemoteException
import android.provider.ContactsContract
import com.samridhi.contactapp.network.model.Contact
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

object AddContacts {
    suspend fun addMultipleContacts(
        contacts: List<Contact>,
        contentResolver: ContentResolver,
        onProgress: (String) -> Unit
    ) {
        for (contact in contacts) {
            val operations = ArrayList<ContentProviderOperation>() // Create a new list for each contact

            // Add the raw contact
            val rawContactInsertIndex = operations.size
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.RawContacts.CONTENT_URI)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_TYPE, null)
                    .withValue(ContactsContract.RawContacts.ACCOUNT_NAME, null)
                    .build()
            )

            // Add the contact name
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex
                    )
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.StructuredName.CONTENT_ITEM_TYPE
                    )
                    .withValue(
                        ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME,
                        contact.name
                    )
                    .build()
            )

            // Add the contact number
            operations.add(
                ContentProviderOperation.newInsert(ContactsContract.Data.CONTENT_URI)
                    .withValueBackReference(
                        ContactsContract.Data.RAW_CONTACT_ID,
                        rawContactInsertIndex
                    )
                    .withValue(
                        ContactsContract.Data.MIMETYPE,
                        ContactsContract.CommonDataKinds.Phone.CONTENT_ITEM_TYPE
                    )
                    .withValue(ContactsContract.CommonDataKinds.Phone.NUMBER, contact.number)
                    .withValue(
                        ContactsContract.CommonDataKinds.Phone.TYPE,
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE
                    )
                    .build()
            )

            // Perform batch operation for the current contact
            try {
                withContext(Dispatchers.IO) {
                    contentResolver.applyBatch(ContactsContract.AUTHORITY, operations)
                }
                withContext(Dispatchers.Main) {
                    onProgress("${contact.name} added")
                }
            } catch (e: RemoteException) {
                onProgress("Failed to add ${contact.name}: ${e.message}")
                e.printStackTrace()
            } catch (e: OperationApplicationException) {
                onProgress("Failed to add ${contact.name}: ${e.message}")
                e.printStackTrace()
            }
        }
    }
}