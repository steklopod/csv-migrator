package csv.migrator.model

data class Migration(
    val TAB_NUMBER: String?,

    val `E-Mail`: String?,
    val GUID: String?,
    val LOGIN_SUDIR: String?,
    val NAME: String?,
    val NEW_LOGIN: String?,
    val OLD_LOGIN: String?,
    val SOURCEDOMAIN: String?,
)