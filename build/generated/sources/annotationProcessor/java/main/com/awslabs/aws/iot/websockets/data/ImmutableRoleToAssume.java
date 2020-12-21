package com.awslabs.aws.iot.websockets.data;

import java.util.Objects;
import java.util.Optional;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link RoleToAssume}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableRoleToAssume.builder()}.
 */
@Generated(from = "RoleToAssume", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableRoleToAssume extends RoleToAssume {
  private final String roleToAssume;

  private ImmutableRoleToAssume(String roleToAssume) {
    this.roleToAssume = roleToAssume;
  }

  /**
   * @return The value of the {@code roleToAssume} attribute
   */
  @Override
  public Optional<String> getRoleToAssume() {
    return Optional.ofNullable(roleToAssume);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link RoleToAssume#getRoleToAssume() roleToAssume} attribute.
   * @param value The value for roleToAssume
   * @return A modified copy of {@code this} object
   */
  public final ImmutableRoleToAssume withRoleToAssume(String value) {
    String newValue = Objects.requireNonNull(value, "roleToAssume");
    if (Objects.equals(this.roleToAssume, newValue)) return this;
    return new ImmutableRoleToAssume(newValue);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link RoleToAssume#getRoleToAssume() roleToAssume} attribute.
   * An equality check is used on inner nullable value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for roleToAssume
   * @return A modified copy of {@code this} object
   */
  public final ImmutableRoleToAssume withRoleToAssume(Optional<String> optional) {
    String value = optional.orElse(null);
    if (Objects.equals(this.roleToAssume, value)) return this;
    return new ImmutableRoleToAssume(value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableRoleToAssume} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableRoleToAssume
        && equalTo((ImmutableRoleToAssume) another);
  }

  private boolean equalTo(ImmutableRoleToAssume another) {
    return Objects.equals(roleToAssume, another.roleToAssume);
  }

  /**
   * Computes a hash code from attributes: {@code roleToAssume}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + Objects.hashCode(roleToAssume);
    return h;
  }

  /**
   * Creates an immutable copy of a {@link RoleToAssume} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable RoleToAssume instance
   */
  public static ImmutableRoleToAssume copyOf(RoleToAssume instance) {
    if (instance instanceof ImmutableRoleToAssume) {
      return (ImmutableRoleToAssume) instance;
    }
    return ImmutableRoleToAssume.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableRoleToAssume ImmutableRoleToAssume}.
   * <pre>
   * ImmutableRoleToAssume.builder()
   *    .roleToAssume(String) // optional {@link RoleToAssume#getRoleToAssume() roleToAssume}
   *    .build();
   * </pre>
   * @return A new ImmutableRoleToAssume builder
   */
  public static ImmutableRoleToAssume.Builder builder() {
    return new ImmutableRoleToAssume.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableRoleToAssume ImmutableRoleToAssume}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "RoleToAssume", generator = "Immutables")
  public static final class Builder {
    private String roleToAssume;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code RoleToAssume} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(RoleToAssume instance) {
      Objects.requireNonNull(instance, "instance");
      Optional<String> roleToAssumeOptional = instance.getRoleToAssume();
      if (roleToAssumeOptional.isPresent()) {
        roleToAssume(roleToAssumeOptional);
      }
      return this;
    }

    /**
     * Initializes the optional value {@link RoleToAssume#getRoleToAssume() roleToAssume} to roleToAssume.
     * @param roleToAssume The value for roleToAssume
     * @return {@code this} builder for chained invocation
     */
    public final Builder roleToAssume(String roleToAssume) {
      this.roleToAssume = Objects.requireNonNull(roleToAssume, "roleToAssume");
      return this;
    }

    /**
     * Initializes the optional value {@link RoleToAssume#getRoleToAssume() roleToAssume} to roleToAssume.
     * @param roleToAssume The value for roleToAssume
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder roleToAssume(Optional<String> roleToAssume) {
      this.roleToAssume = roleToAssume.orElse(null);
      return this;
    }

    /**
     * Builds a new {@link ImmutableRoleToAssume ImmutableRoleToAssume}.
     * @return An immutable instance of RoleToAssume
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableRoleToAssume build() {
      return new ImmutableRoleToAssume(roleToAssume);
    }
  }
}
