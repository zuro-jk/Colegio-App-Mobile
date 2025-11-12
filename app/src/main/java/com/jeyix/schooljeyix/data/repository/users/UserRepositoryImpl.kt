package com.jeyix.schooljeyix.data.repository.users

import android.content.Context
import android.net.Uri
import com.jeyix.schooljeyix.data.remote.feature.auth.response.UserProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.api.UserApi
import com.jeyix.schooljeyix.data.remote.feature.users.request.ChangePasswordRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.CreateAdminRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.DeviceTokenRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateProfileRequest
import com.jeyix.schooljeyix.data.remote.feature.users.request.UpdateUserRequest
import com.jeyix.schooljeyix.data.remote.feature.users.response.UpdateProfileResponse
import com.jeyix.schooljeyix.data.remote.feature.users.response.UserSessionResponse
import com.jeyix.schooljeyix.data.util.FileUtil
import com.jeyix.schooljeyix.domain.repository.UserRepository
import com.jeyix.schooljeyix.domain.util.Resource
import dagger.hilt.android.qualifiers.ApplicationContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class UserRepositoryImpl @Inject constructor(
    private val api: UserApi,
    private val fileUtil: FileUtil,
    @ApplicationContext private val context: Context
): UserRepository {

    override suspend fun getMeData(): Resource<UserProfileResponse> {
        return try {
            val response = api.getMeData()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener datos del perfil.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getMySessions(): Resource<List<UserSessionResponse>> {
        return try {
            val response = api.getMySessions()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener las sesiones.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getAllPersonalForAdmin(): Resource<List<UserProfileResponse>> {
        return try {
            val response = api.getAllPersonalForAdmin()
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al obtener el personal.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun getUserById(userId: Long): Resource<UserProfileResponse> {
        return try {
            val response = api.getUserById(userId)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Usuario no encontrado.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateDeviceToken(deviceTokenRequest: DeviceTokenRequest): Resource<Unit> {
        return try {
            val response = api.updateDeviceToken(deviceTokenRequest)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al actualizar el token del dispositivo.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun changePassword(changePasswordRequest: ChangePasswordRequest): Resource<Unit> {
        return try {
            val response = api.changePassword(changePasswordRequest)
            if (response.isSuccessful) {
                Resource.Success(Unit)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al cambiar la contraseña.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateProfile(updateProfileRequest: UpdateProfileRequest): Resource<UpdateProfileResponse> {
        return try {
            val response = api.updateProfile(updateProfileRequest)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al actualizar el perfil.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateProfileImage(imageUri: Uri): Resource<UserProfileResponse> {
        return try {
            val inputStream = context.contentResolver.openInputStream(imageUri)
            val fileBytes = inputStream?.readBytes()
            inputStream?.close()

            if (fileBytes == null) {
                return Resource.Error("No se pudo leer el archivo de imagen.")
            }

            val mimeType = context.contentResolver.getType(imageUri)
            val requestFile = fileBytes.toRequestBody(mimeType?.toMediaTypeOrNull())
            val bodyPart = MultipartBody.Part.createFormData("file", "profile_image.jpg", requestFile)

            val response = api.updateProfileImage(bodyPart)

            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al subir la imagen.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión al subir la imagen.")
        }
    }

    override suspend fun createAdminUser(request: CreateAdminRequest): Resource<UserProfileResponse> {
        return try {
            val response = api.createAdminUser(request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al crear el usuario.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateUserById(userId: Long, request: UpdateUserRequest): Resource<UserProfileResponse> {
        return try {
            val response = api.updateUserById(userId, request)
            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al actualizar el usuario.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión.")
        }
    }

    override suspend fun updateUserImageById(userId: Long, imageUri: Uri): Resource<UserProfileResponse> {
        return try {
            val bodyPart = fileUtil.createMultipartBody(imageUri, "file")
                ?: return Resource.Error("No se pudo leer el archivo de imagen.")

            val response = api.updateUserImageById(userId, bodyPart)

            if (response.isSuccessful && response.body()?.data != null) {
                Resource.Success(response.body()!!.data!!)
            } else {
                Resource.Error(response.errorBody()?.string() ?: "Error al subir la imagen.")
            }
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Error de conexión al subir la imagen.")
        }
    }


}