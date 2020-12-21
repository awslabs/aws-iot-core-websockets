package com.awslabs.aws.iot.websockets.data;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import org.immutables.value.Generated;

/**
 * Immutable implementation of {@link ClientId}.
 * <p>
 * Use the builder to create immutable instances:
 * {@code ImmutableClientId.builder()}.
 */
@Generated(from = "ClientId", generator = "Immutables")
@SuppressWarnings({"all"})
@javax.annotation.processing.Generated("org.immutables.processor.ProxyProcessor")
public final class ImmutableClientId extends ClientId {
  private final String clientId;

  private ImmutableClientId(String clientId) {
    this.clientId = clientId;
  }

  /**
   * @return The value of the {@code clientId} attribute
   */
  @Override
  public String getClientId() {
    return clientId;
  }

  /**
   * Copy the current immutable object by setting a value for the {@link ClientId#getClientId() clientId} attribute.
   * An equals check used to prevent copying of the same value by returning {@code this}.
   * @param value A new value for clientId
   * @return A modified copy of the {@code this} object
   */
  public final ImmutableClientId withClientId(String value) {
    String newValue = Objects.requireNonNull(value, "clientId");
    if (this.clientId.equals(newValue)) return this;
    return new ImmutableClientId(newValue);
  }

  /**
   * This instance is equal to all instances of {@code ImmutableClientId} that have equal attribute values.
   * @return {@code true} if {@code this} is equal to {@code another} instance
   */
  @Override
  public boolean equals(Object another) {
    if (this == another) return true;
    return another instanceof ImmutableClientId
        && equalTo((ImmutableClientId) another);
  }

  private boolean equalTo(ImmutableClientId another) {
    return clientId.equals(another.clientId);
  }

  /**
   * Computes a hash code from attributes: {@code clientId}.
   * @return hashCode value
   */
  @Override
  public int hashCode() {
    int h = 5381;
    h += (h << 5) + clientId.hashCode();
    return h;
  }

  /**
   * Creates an immutable copy of a {@link ClientId} value.
   * Uses accessors to get values to initialize the new immutable instance.
   * If an instance is already immutable, it is returned as is.
   * @param instance The instance to copy
   * @return A copied immutable ClientId instance
   */
  public static ImmutableClientId copyOf(ClientId instance) {
    if (instance instanceof ImmutableClientId) {
      return (ImmutableClientId) instance;
    }
    return ImmutableClientId.builder()
        .from(instance)
        .build();
  }

  /**
   * Creates a builder for {@link ImmutableClientId ImmutableClientId}.
   * <pre>
   * ImmutableClientId.builder()
   *    .clientId(String) // required {@link ClientId#getClientId() clientId}
   *    .build();
   * </pre>
   * @return A new ImmutableClientId builder
   */
  public static ImmutableClientId.Builder builder() {
    return new ImmutableClientId.Builder();
  }

  /**
   * Builds instances of type {@link ImmutableClientId ImmutableClientId}.
   * Initialize attributes and then invoke the {@link #build()} method to create an
   * immutable instance.
   * <p><em>{@code Builder} is not thread-safe and generally should not be stored in a field or collection,
   * but instead used immediately to create instances.</em>
   */
  @Generated(from = "ClientId", generator = "Immutables")
  public static final class Builder {
    private static final long INIT_BIT_CLIENT_ID = 0x1L;
    private long initBits = 0x1L;

    private String clientId;

    private Builder() {
    }

    /**
     * Fill a builder with attribute values from the provided {@code ClientId} instance.
     * Regular attribute values will be replaced with those from the given instance.
     * Absent optional values will not replace present values.
     * @param instance The instance from which to copy values
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder from(ClientId instance) {
      Objects.requireNonNull(instance, "instance");
      clientId(instance.getClientId());
      return this;
    }

    /**
     * Initializes the value for the {@link ClientId#getClientId() clientId} attribute.
     * @param clientId The value for clientId 
     * @return {@code this} builder for use in a chained invocation
     */
    public final Builder clientId(String clientId) {
      this.clientId = Objects.requireNonNull(clientId, "clientId");
      initBits &= ~INIT_BIT_CLIENT_ID;
      return this;
    }

    /**
     * Builds a new {@link ImmutableClientId ImmutableClientId}.
     * @return An immutable instance of ClientId
     * @throws java.lang.IllegalStateException if any required attributes are missing
     */
    public ImmutableClientId build() {
      if (initBits != 0) {
        throw new IllegalStateException(formatRequiredAttributesMessage());
      }
      return new ImmutableClientId(clientId);
    }

    private String formatRequiredAttributesMessage() {
      List<String> attributes = new ArrayList<>();
      if ((initBits & INIT_BIT_CLIENT_ID) != 0) attributes.add("clientId");
      return "Cannot build ClientId, some of required attributes are not set " + attributes;
    }
  }
}
