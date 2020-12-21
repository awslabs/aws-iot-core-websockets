package com.awslabs.aws.iot.websockets.data;

import java.util.Objects;
import java.util.Optional;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link UsernamePassword}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableUsernamePassword.builder()}.
 */
@Generated(from = "UsernamePassword", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableUsernamePassword extends UsernamePassword {
  private final String username;
  private final char[] password;

  private ImmutableUsernamePassword(String username, char[] password) {
    this.username = username;
    this.password = password;
  }

  /**
   * @return The value of the {@code username} attribute
   */
  @Override
  public Optional<String> getUsername() {
    return Optional.ofNullable(username);
  }

  /**
   * @return The value of the {@code password} attribute
   */
  @Override
  public Optional<char[]> getPassword() {
    return Optional.ofNullable(password);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link UsernamePassword#getUsername() username} attribute.
   * @param value The value for username
   * @return A modified copy of {@code this} object
   */
  public final ImmutableUsernamePassword withUsername(String value) {
    String newValue = Objects.requireNonNull(value, "username");
    if (Objects.equals(this.username, newValue)) return this;
    return new ImmutableUsernamePassword(newValue, this.password);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link UsernamePassword#getUsername() username} attribute.
   * An equality check is used on inner nullable value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for username
   * @return A modified copy of {@code this} object
   */
  public final ImmutableUsernamePassword withUsername(Optional<String> optional) {
    String value = optional.orElse(null);
    if (Objects.equals(this.username, value)) return this;
    return new ImmutableUsernamePassword(value, this.password);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link UsernamePassword#getPassword() password} attribute.
   * @param value The value for password
   * @return A modified copy of {@code this} object
   */
  public final ImmutableUsernamePassword withPassword(char[] value) {
    char[] newValue = Objects.requireNonNull(value, "password");
    if (this.password == newValue) return this;
    return new ImmutableUsernamePassword(this.username, newValue);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link UsernamePassword#getPassword() password} attribute.
   * A shallow reference equality check is used on unboxed optional value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for password
   * @return A modified copy of {@code this} object
   */
  @SuppressWarnings("unchecked") // safe covariant cast
  public final ImmutableUsernamePassword withPassword(Optional<? extends char[]> optional) {
    char[] value = optional.orElse(null);
    if (this.password == value) return this;
    return new ImmutableUsernamePassword(this.username, value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableUsernamePassword} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableUsernamePassword
        && equalTo((ImmutableUsernamePassword) another);
  }

  private boolean equalTo(ImmutableUsernamePassword another) {
    return Objects.equals(username, another.username)
        && Objects.equals(password, another.password);
  }

  /**
   * Computes a hash code from attributes: {@code username}, {@code password}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + Objects.hashCode(username);
    h += (h << 5) + Objects.hashCode(password);
    return h;
  }

  /**
   * Creates an immutable copy of a {@link UsernamePassword} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable UsernamePassword instance
   */
  public static ImmutableUsernamePassword copyOf(UsernamePassword instance) {
    if (instance instanceof ImmutableUsernamePassword) {
      return (ImmutableUsernamePassword) instance;
    }
    return ImmutableUsernamePassword.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableUsernamePassword ImmutableUsernamePassword}.
   * <pre>
   * ImmutableUsernamePassword.builder()
   *    .username(String) // optional {@link UsernamePassword#getUsername() username}
   *    .password(char[]) // optional {@link UsernamePassword#getPassword() password}
   *    .build();
   * </pre>
   * @return A new ImmutableUsernamePassword builder
   */
  public static ImmutableUsernamePassword.Builder builder() {
    return new ImmutableUsernamePassword.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableUsernamePassword ImmutableUsernamePassword}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "UsernamePassword", generator = "Immutables")
  public static final class Builder {
    private String username;
    private char[] password;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code UsernamePassword} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(UsernamePassword instance) {
      Objects.requireNonNull(instance, "instance");
      Optional<String> usernameOptional = instance.getUsername();
      if (usernameOptional.isPresent()) {
        username(usernameOptional);
      }
      Optional<char[]> passwordOptional = instance.getPassword();
      if (passwordOptional.isPresent()) {
        password(passwordOptional);
      }
      return this;
    }

    /**
     * Initializes the optional value {@link UsernamePassword#getUsername() username} to username.
     * @param username The value for username
     * @return {@code this} builder for chained invocation
     */
    public final Builder username(String username) {
      this.username = Objects.requireNonNull(username, "username");
      return this;
    }

    /**
     * Initializes the optional value {@link UsernamePassword#getUsername() username} to username.
     * @param username The value for username
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder username(Optional<String> username) {
      this.username = username.orElse(null);
      return this;
    }

    /**
     * Initializes the optional value {@link UsernamePassword#getPassword() password} to password.
     * @param password The value for password
     * @return {@code this} builder for chained invocation
     */
    public final Builder password(char[] password) {
      this.password = Objects.requireNonNull(password, "password");
      return this;
    }

    /**
     * Initializes the optional value {@link UsernamePassword#getPassword() password} to password.
     * @param password The value for password
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder password(Optional<? extends char[]> password) {
      this.password = password.orElse(null);
      return this;
    }

    /**
     * Builds a new {@link ImmutableUsernamePassword ImmutableUsernamePassword}.
     * @return An immutable instance of UsernamePassword
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableUsernamePassword build() {
      return new ImmutableUsernamePassword(username, password);
    }
  }
}
