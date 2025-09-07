package com.legacykeep.user.entity;

import com.legacykeep.user.security.EncryptedStringConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Profile entity for personal information and bio data.
 * 
 * This entity contains user profile information including personal details,
 * contact information, and profile settings. Sensitive data is encrypted
 * using the EncryptedStringConverter.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "user_profiles", indexes = {
    @Index(name = "idx_user_profiles_user_id", columnList = "user_id"),
    @Index(name = "idx_user_profiles_display_name", columnList = "display_name"),
    @Index(name = "idx_user_profiles_is_public", columnList = "is_public"),
    @Index(name = "idx_user_profiles_created_at", columnList = "created_at"),
    @Index(name = "idx_user_profiles_deleted_at", columnList = "deleted_at"),
    @Index(name = "idx_user_profiles_first_name_hash", columnList = "first_name_hash"),
    @Index(name = "idx_user_profiles_last_name_hash", columnList = "last_name_hash"),
    @Index(name = "idx_user_profiles_phone_number_hash", columnList = "phone_number_hash")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(exclude = {"bio"})
public class UserProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false, unique = true)
    private Long userId; // References auth service user ID

    @Size(max = 50, message = "First name must not exceed 50 characters")
    @Column(name = "first_name", length = 50)
    @Convert(converter = EncryptedStringConverter.class)
    private String firstName;

    @Column(name = "first_name_hash", length = 64)
    private String firstNameHash;

    @Size(max = 50, message = "Last name must not exceed 50 characters")
    @Column(name = "last_name", length = 50)
    @Convert(converter = EncryptedStringConverter.class)
    private String lastName;

    @Column(name = "last_name_hash", length = 64)
    private String lastNameHash;

    @Size(max = 100, message = "Display name must not exceed 100 characters")
    @Column(name = "display_name", length = 100)
    private String displayName;

    @Column(name = "bio", columnDefinition = "TEXT")
    private String bio;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Size(max = 20, message = "Phone number must not exceed 20 characters")
    @Column(name = "phone_number", length = 20)
    @Convert(converter = EncryptedStringConverter.class)
    private String phoneNumber;

    @Column(name = "phone_number_hash", length = 64)
    private String phoneNumberHash;

    @Size(max = 255, message = "Address line 1 must not exceed 255 characters")
    @Column(name = "address_line1", length = 255)
    private String addressLine1;

    @Size(max = 255, message = "Address line 2 must not exceed 255 characters")
    @Column(name = "address_line2", length = 255)
    private String addressLine2;

    @Size(max = 100, message = "City must not exceed 100 characters")
    @Column(name = "city", length = 100)
    private String city;

    @Size(max = 100, message = "State must not exceed 100 characters")
    @Column(name = "state", length = 100)
    private String state;

    @Size(max = 100, message = "Country must not exceed 100 characters")
    @Column(name = "country", length = 100)
    private String country;

    @Size(max = 20, message = "Postal code must not exceed 20 characters")
    @Column(name = "postal_code", length = 20)
    private String postalCode;

    @Size(max = 50, message = "Timezone must not exceed 50 characters")
    @Column(name = "timezone", length = 50)
    private String timezone = "UTC";

    @Size(max = 10, message = "Language must not exceed 10 characters")
    @Column(name = "language", length = 10)
    private String language = "en";

    @Size(max = 500, message = "Profile picture URL must not exceed 500 characters")
    @Column(name = "profile_picture_url", length = 500)
    private String profilePictureUrl;

    @Size(max = 500, message = "Profile picture thumbnail URL must not exceed 500 characters")
    @Column(name = "profile_picture_thumbnail_url", length = 500)
    private String profilePictureThumbnailUrl;

    @Column(name = "is_public", nullable = false)
    private boolean isPublic = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "deleted_at")
    private LocalDateTime deletedAt;

    @Version
    @Column(nullable = false)
    private Long version = 0L;

    // =============================================================================
    // Constructors
    // =============================================================================

    public UserProfile() {
        // Default constructor for JPA
    }

    public UserProfile(Long userId) {
        this.userId = userId;
    }

    // =============================================================================
    // Business Methods
    // =============================================================================

    /**
     * Get the full name by combining first and last name.
     * 
     * @return the full name, or display name if available
     */
    public String getFullName() {
        if (displayName != null && !displayName.trim().isEmpty()) {
            return displayName;
        }
        
        StringBuilder fullName = new StringBuilder();
        if (firstName != null && !firstName.trim().isEmpty()) {
            fullName.append(firstName.trim());
        }
        if (lastName != null && !lastName.trim().isEmpty()) {
            if (fullName.length() > 0) {
                fullName.append(" ");
            }
            fullName.append(lastName.trim());
        }
        
        return fullName.length() > 0 ? fullName.toString() : "User";
    }

    /**
     * Check if the profile is complete with essential information.
     * 
     * @return true if profile has first name, last name, and bio
     */
    public boolean isComplete() {
        return firstName != null && !firstName.trim().isEmpty() &&
               lastName != null && !lastName.trim().isEmpty() &&
               bio != null && !bio.trim().isEmpty();
    }

    /**
     * Get the age based on date of birth.
     * 
     * @return the age in years, or null if date of birth is not set
     */
    public Integer getAge() {
        if (dateOfBirth == null) {
            return null;
        }
        
        LocalDate now = LocalDate.now();
        int age = now.getYear() - dateOfBirth.getYear();
        
        if (now.getDayOfYear() < dateOfBirth.getDayOfYear()) {
            age--;
        }
        
        return age;
    }

    /**
     * Soft delete the profile by setting deleted_at timestamp.
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * Check if the profile is deleted.
     * 
     * @return true if the profile is soft deleted
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Restore a soft deleted profile.
     */
    public void restore() {
        this.deletedAt = null;
    }

    // =============================================================================
    // Equals and HashCode
    // =============================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserProfile that = (UserProfile) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
