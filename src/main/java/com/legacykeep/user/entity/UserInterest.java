package com.legacykeep.user.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Objects;

/**
 * User Interest entity for user interests, hobbies, and skills.
 * 
 * This entity contains user interests, hobbies, skills, and other
 * personal preferences that can be used for matching and discovery.
 * 
 * @author LegacyKeep Team
 * @version 1.0.0
 */
@Entity
@Table(name = "user_interests", indexes = {
    @Index(name = "idx_user_interests_user_id", columnList = "user_id"),
    @Index(name = "idx_user_interests_type", columnList = "interest_type"),
    @Index(name = "idx_user_interests_name", columnList = "interest_name"),
    @Index(name = "idx_user_interests_is_public", columnList = "is_public"),
    @Index(name = "idx_user_interests_deleted_at", columnList = "deleted_at")
})
@EntityListeners(AuditingEntityListener.class)
@Data
@EqualsAndHashCode(callSuper = false)
@ToString
public class UserInterest {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId; // References auth service user ID

    @NotNull(message = "Interest type is required")
    @Enumerated(EnumType.STRING)
    @Column(name = "interest_type", nullable = false, length = 50)
    private InterestType interestType;

    @NotBlank(message = "Interest name is required")
    @Size(max = 100, message = "Interest name must not exceed 100 characters")
    @Column(name = "interest_name", nullable = false, length = 100)
    private String interestName;

    @Size(max = 500, message = "Interest description must not exceed 500 characters")
    @Column(name = "interest_description", columnDefinition = "TEXT")
    private String interestDescription;

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

    // =============================================================================
    // Enums
    // =============================================================================

    public enum InterestType {
        HOBBY,          // Recreational activities and hobbies
        SKILL,          // Professional or personal skills
        INTEREST,       // General interests and topics
        PASSION,        // Deep passions and causes
        SPORT,          // Sports and physical activities
        ART,            // Arts and creative activities
        MUSIC,          // Music and musical instruments
        TRAVEL,         // Travel and exploration
        FOOD,           // Food and cooking
        TECHNOLOGY,     // Technology and gadgets
        BOOKS,          // Books and reading
        MOVIES,         // Movies and entertainment
        GAMES,          // Games and gaming
        NATURE,         // Nature and outdoor activities
        VOLUNTEER,      // Volunteering and charity
        EDUCATION,      // Learning and education
        BUSINESS,       // Business and entrepreneurship
        HEALTH,         // Health and fitness
        FAMILY,         // Family and relationships
        OTHER           // Other interests
    }

    // =============================================================================
    // Constructors
    // =============================================================================

    public UserInterest() {
        // Default constructor for JPA
    }

    public UserInterest(Long userId, InterestType interestType, String interestName) {
        this.userId = userId;
        this.interestType = interestType;
        this.interestName = interestName;
    }

    // =============================================================================
    // Business Methods
    // =============================================================================

    /**
     * Soft delete the interest by setting deleted_at timestamp.
     */
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    /**
     * Check if the interest is deleted.
     * 
     * @return true if the interest is soft deleted
     */
    public boolean isDeleted() {
        return deletedAt != null;
    }

    /**
     * Restore a soft deleted interest.
     */
    public void restore() {
        this.deletedAt = null;
    }

    /**
     * Check if the interest is a skill type.
     * 
     * @return true if the interest type is SKILL
     */
    public boolean isSkill() {
        return interestType == InterestType.SKILL;
    }

    /**
     * Check if the interest is a hobby type.
     * 
     * @return true if the interest type is HOBBY
     */
    public boolean isHobby() {
        return interestType == InterestType.HOBBY;
    }

    /**
     * Check if the interest is a passion type.
     * 
     * @return true if the interest type is PASSION
     */
    public boolean isPassion() {
        return interestType == InterestType.PASSION;
    }

    /**
     * Get a display-friendly version of the interest type.
     * 
     * @return the formatted interest type
     */
    public String getDisplayType() {
        return interestType.name().toLowerCase().replace("_", " ");
    }

    /**
     * Get the full interest description with type and name.
     * 
     * @return the formatted interest description
     */
    public String getFullDescription() {
        StringBuilder description = new StringBuilder();
        description.append(getDisplayType()).append(": ").append(interestName);
        
        if (interestDescription != null && !interestDescription.trim().isEmpty()) {
            description.append(" - ").append(interestDescription);
        }
        
        return description.toString();
    }

    // =============================================================================
    // Equals and HashCode
    // =============================================================================

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserInterest that = (UserInterest) o;
        return Objects.equals(id, that.id) && Objects.equals(userId, that.userId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userId);
    }
}
