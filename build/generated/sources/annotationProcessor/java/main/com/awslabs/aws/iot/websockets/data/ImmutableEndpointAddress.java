package com.awslabs.aws.iot.websockets.data;

import java.util.Objects;
import java.util.Optional;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link EndpointAddress}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableEndpointAddress.builder()}.
 */
@Generated(from = "EndpointAddress", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableEndpointAddress extends EndpointAddress {
  private final String endpointAddress;

  private ImmutableEndpointAddress(String endpointAddress) {
    this.endpointAddress = endpointAddress;
  }

  /**
   * @return The value of the {@code endpointAddress} attribute
   */
  @Override
  public Optional<String> getEndpointAddress() {
    return Optional.ofNullable(endpointAddress);
  }

  /**
   * Copy the current immutable object by setting a <i>present</i> value for the optional {@link EndpointAddress#getEndpointAddress() endpointAddress} attribute.
   * @param value The value for endpointAddress
   * @return A modified copy of {@code this} object
   */
  public final ImmutableEndpointAddress withEndpointAddress(String value) {
    String newValue = Objects.requireNonNull(value, "endpointAddress");
    if (Objects.equals(this.endpointAddress, newValue)) return this;
    return new ImmutableEndpointAddress(newValue);
  }

  /**
   * Copy the current immutable object by setting an optional value for the {@link EndpointAddress#getEndpointAddress() endpointAddress} attribute.
   * An equality check is used on inner nullable value to prevent copying of the same value by returning {@code this}.
   * @param optional A value for endpointAddress
   * @return A modified copy of {@code this} object
   */
  public final ImmutableEndpointAddress withEndpointAddress(Optional<String> optional) {
    String value = optional.orElse(null);
    if (Objects.equals(this.endpointAddress, value)) return this;
    return new ImmutableEndpointAddress(value);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableEndpointAddress} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableEndpointAddress
        && equalTo((ImmutableEndpointAddress) another);
  }

  private boolean equalTo(ImmutableEndpointAddress another) {
    return Objects.equals(endpointAddress, another.endpointAddress);
  }

  /**
   * Computes a hash code from attributes: {@code endpointAddress}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + Objects.hashCode(endpointAddress);
    return h;
  }

  /**
   * Creates an immutable copy of a {@link EndpointAddress} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable EndpointAddress instance
   */
  public static ImmutableEndpointAddress copyOf(EndpointAddress instance) {
    if (instance instanceof ImmutableEndpointAddress) {
      return (ImmutableEndpointAddress) instance;
    }
    return ImmutableEndpointAddress.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableEndpointAddress ImmutableEndpointAddress}.
   * <pre>
   * ImmutableEndpointAddress.builder()
   *    .endpointAddress(String) // optional {@link EndpointAddress#getEndpointAddress() endpointAddress}
   *    .build();
   * </pre>
   * @return A new ImmutableEndpointAddress builder
   */
  public static ImmutableEndpointAddress.Builder builder() {
    return new ImmutableEndpointAddress.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableEndpointAddress ImmutableEndpointAddress}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "EndpointAddress", generator = "Immutables")
  public static final class Builder {
    private String endpointAddress;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code EndpointAddress} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(EndpointAddress instance) {
      Objects.requireNonNull(instance, "instance");
      Optional<String> endpointAddressOptional = instance.getEndpointAddress();
      if (endpointAddressOptional.isPresent()) {
        endpointAddress(endpointAddressOptional);
      }
      return this;
    }

    /**
     * Initializes the optional value {@link EndpointAddress#getEndpointAddress() endpointAddress} to endpointAddress.
     * @param endpointAddress The value for endpointAddress
     * @return {@code this} builder for chained invocation
     */
    public final Builder endpointAddress(String endpointAddress) {
      this.endpointAddress = Objects.requireNonNull(endpointAddress, "endpointAddress");
      return this;
    }

    /**
     * Initializes the optional value {@link EndpointAddress#getEndpointAddress() endpointAddress} to endpointAddress.
     * @param endpointAddress The value for endpointAddress
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder endpointAddress(Optional<String> endpointAddress) {
      this.endpointAddress = endpointAddress.orElse(null);
      return this;
    }

    /**
     * Builds a new {@link ImmutableEndpointAddress ImmutableEndpointAddress}.
     * @return An immutable instance of EndpointAddress
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableEndpointAddress build() {
      return new ImmutableEndpointAddress(endpointAddress);
    }
  }
}
