package fxms.nms.co.snmp.mib;

public class ENTITY_MIB {
	/**
	 * "This object is an 'alias' name for the physical entity, as specified by
	 * a network manager, and provides a non-volatile 'handle' for the physical
	 * entity.
	 * 
	 * On the first instantiation of a physical entity, the value
	 * 
	 * 
	 * of entPhysicalAlias associated with that entity is set to the zero-length
	 * string. However, the agent may set the value to a locally unique default
	 * value, instead of a zero-length string.
	 * 
	 * If write access is implemented for an instance of entPhysicalAlias, and a
	 * value is written into the instance, the agent must retain the supplied
	 * value in the entPhysicalAlias instance (associated with the same physical
	 * entity) for as long as that entity remains instantiated. This includes
	 * instantiations across all re-initializations/reboots of the network
	 * management system, including those resulting in a change of the physical
	 * entity's entPhysicalIndex value."
	 * 
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-write <br>
	 * Status current
	 * 
	 */
	public final String entPhysicalAlias = ".1.3.6.1.2.1.47.1.1.1.1.14";

	/**
	 * "This object is a user-assigned asset tracking identifier (as specified
	 * by a network manager) for the physical entity, and provides non-volatile
	 * storage of this information.
	 * 
	 * On the first instantiation of a physical entity, the value of
	 * entPhysicalAssetID associated with that entity is set to the zero-length
	 * string.
	 * 
	 * Not every physical component will have an asset tracking identifier, or
	 * even need one. Physical entities for which the associated value of the
	 * entPhysicalIsFRU object is equal to 'false(2)' (e.g., the repeater ports
	 * within a repeater module), do not need their own unique asset tracking
	 * identifier. An agent does not have to provide write access for such
	 * entities, and may instead return a zero-length string.
	 * 
	 * If write access is implemented for an instance of entPhysicalAssetID, and
	 * a value is written into the instance, the agent must retain the supplied
	 * value in the entPhysicalAssetID instance (associated with the same
	 * physical entity) for as long as that entity remains instantiated. This
	 * includes instantiations across all re-initializations/reboots of the
	 * network management system, including those resulting in a change of the
	 * physical entity's entPhysicalIndex value.
	 * 
	 * 
	 * If no asset tracking information is associated with the physical
	 * component, then this object will contain a zero-length string."
	 * 
	 * Type SnmpAdminString <br>
	 * Permission read-write <br>
	 * Status current
	 * 
	 */
	public final String entPhysicalAssetID = ".1.3.6.1.2.1.47.1.1.1.1.15";

	/**
	 * 
	 * OID 1.3.6.1.2.1.47.1.1.1.1.5<br>
	 * Type PhysicalClass <br>
	 * 1:other<br>
	 * 2:unknown<br>
	 * 3:chassis<br>
	 * 4:backplane<br>
	 * 5:container<br>
	 * 6:powerSupply<br>
	 * 7:fan<br>
	 * 8:sensor<br>
	 * 9:module<br>
	 * 10:port<br>
	 * 11:stack<br>
	 * 12:cpu<br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "An indication of
	 * the general hardware type of the physical entity.
	 * 
	 * An agent should set this object to the standard enumeration value that
	 * most accurately indicates the general class of the physical entity, or
	 * the primary class if there is more than one entity.
	 * 
	 * If no appropriate standard registration identifier exists for this
	 * physical entity, then the value 'other(1)' is returned. If the value is
	 * unknown by this agent, then the value 'unknown(2)' is returned."
	 * 
	 */
	public final String entPhysicalClass = ".1.3.6.1.2.1.47.1.1.1.1.5";

	/** 1 */
	public final int entPhysicalClass_other = 1;
	/** 2 */
	public final int entPhysicalClass_unknown = 2;
	/** 3 */
	public final int entPhysicalClass_chassis = 3;
	/** 4 */
	public final int entPhysicalClass_backplane = 4;
	/** 5 */
	public final int entPhysicalClass_container = 5;
	/** 6 */
	public final int entPhysicalClass_powerSupply = 6;
	/** 7 */
	public final int entPhysicalClass_fan = 7;
	/** 8 */
	public final int entPhysicalClass_sensor = 8;
	/** 9 */
	public final int entPhysicalClass_module = 9;
	/** 10 */
	public final int entPhysicalClass_port = 10;
	/** 11 */
	public final int entPhysicalClass_stack = 11;
	/** 12 */
	public final int entPhysicalClass_cpu = 12;

	/**
	 * The value of entPhysicalIndex for the physical entity which 'contains'
	 * this physical entity. A value of zero indicates this physical entity is
	 * not contained in any other physical entity. Note that the set of
	 * 'containment' relationships define a strict hierarchy; that is, recursion
	 * is not allowed.
	 * 
	 * In the event that a physical entity is contained by more than one
	 * physical entity (e.g., double-wide modules), this object should identify
	 * the containing entity with the lowest value of entPhysicalIndex.
	 * 
	 * Type PhysicalIndexOrZero
	 * 
	 * Permission read-only
	 * 
	 * Status current
	 * 
	 */
	public final String entPhysicalContainedIn = ".1.3.6.1.2.1.47.1.1.1.1.4";

	/**
	 * OID 1.3.6.1.2.1.47.1.1.1.1.2 <br>
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "A textual
	 * description of physical entity. This object should contain a string that
	 * identifies the manufacturer's name for the physical entity, and should be
	 * set to a distinct value for each version or model of the physical
	 * entity."
	 */
	public final String entPhysicalDescr = ".1.3.6.1.2.1.47.1.1.1.1.2";

	/**
	 * OID 1.3.6.1.2.1.47.1.1.1.1.9<br>
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "The
	 * vendor-specific firmware revision string for the physical entity.
	 * 
	 * Note that if revision information is stored internally in a non-printable
	 * (e.g., binary) format, then the agent must convert such information to a
	 * printable format, in an implementation-specific manner.
	 * 
	 * If no specific firmware programs are associated with the physical
	 * component, or if this information is unknown to the agent, then this
	 * object will contain a zero-length string."
	 */
	public final String entPhysicalFirmwareRev = ".1.3.6.1.2.1.47.1.1.1.1.9";

	/**
	 * OID 1.3.6.1.2.1.47.1.1.1.1.8 <br>
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "The
	 * vendor-specific hardware revision string for the physical entity. The
	 * preferred value is the hardware revision identifier actually printed on
	 * the component itself (if present).
	 * 
	 * Note that if revision information is stored internally in a non-printable
	 * (e.g., binary) format, then the agent must convert such information to a
	 * printable format, in an implementation-specific manner.
	 * 
	 * If no specific hardware revision string is associated with the physical
	 * component, or if this information is unknown to the agent, then this
	 * object will contain a zero-length string."
	 */
	public final String entPhysicalHardwareRev = ".1.3.6.1.2.1.47.1.1.1.1.8";

	/**
	 * "This object indicates whether or not this physical entity is considered
	 * a 'field replaceable unit' by the vendor. If this object contains the
	 * value 'true(1)' then this entPhysicalEntry identifies a field replaceable
	 * unit. For all entPhysicalEntries that represent components permanently
	 * contained within a field replaceable unit, the value 'false(2)' should be
	 * returned for this object."
	 * 
	 * Type TruthValue <br>
	 * 1:true<br>
	 * 2:false<br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * 
	 */
	public final String entPhysicalIsFRU = ".1.3.6.1.2.1.47.1.1.1.1.16";

	/**
	 * "This object contains the date of manufacturing of the managed entity. If
	 * the manufacturing date is unknown or not supported, the object is not
	 * instantiated. The special value '0000000000000000'H may also be returned
	 * in this case."
	 * 
	 * Type DateAndTime <br>
	 * 
	 * Permission read-only <br>
	 * Status current
	 * 
	 */

	public final String entPhysicalMfgDate = ".1.3.6.1.2.1.47.1.1.1.1.17";

	/**
	 * "The name of the manufacturer of this physical component. The preferred
	 * value is the manufacturer name string actually printed on the component
	 * itself (if present).
	 * 
	 * Note that comparisons between instances of the entPhysicalModelName,
	 * entPhysicalFirmwareRev, entPhysicalSoftwareRev, and the
	 * entPhysicalSerialNum objects, are only meaningful amongst
	 * entPhysicalEntries with the same value of entPhysicalMfgName.
	 * 
	 * If the manufacturer name string associated with the physical component is
	 * unknown to the agent, then this object will contain a zero-length
	 * string."
	 * 
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-only <br>
	 * Status current
	 * 
	 */
	public final String entPhysicalMfgName = ".1.3.6.1.2.1.47.1.1.1.1.12";

	/**
	 * OID 1.3.6.1.2.1.47.1.1.1.1.13<br>
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "The
	 * vendor-specific model name identifier string associated with this
	 * physical component. The preferred value is the customer-visible part
	 * number, which may be printed on the component itself.
	 * 
	 * If the model name string associated with the physical component is
	 * unknown to the agent, then this object will contain a zero-length
	 * string."
	 */
	public final String entPhysicalModelName = ".1.3.6.1.2.1.47.1.1.1.1.13";

	/**
	 * Object entPhysicalName <br>
	 * OID 1.3.6.1.2.1.47.1.1.1.1.7 <br>
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * 
	 * MIB ENTITY-MIB ; - View Supporting Images Description "The textual name
	 * of the physical entity. The value of this object should be the name of
	 * the component as assigned by the local device and should be suitable for
	 * use in commands entered at the device's `console'. This might be a text
	 * name (e.g., `console') or a simple component number (e.g., port or module
	 * number, such as `1'), depending on the physical component naming syntax
	 * of the device.
	 * 
	 * If there is no local name, or if this object is otherwise not applicable,
	 * then this object contains a zero-length string.
	 * 
	 * Note that the value of entPhysicalName for two physical entities will be
	 * the same in the event that the console interface does not distinguish
	 * between them, e.g., slot-1 and the card in slot-1."
	 * 
	 */
	public final String entPhysicalName = ".1.3.6.1.2.1.47.1.1.1.1.7";

	/**
	 * An indication of the relative position of this 'child' component among
	 * all its 'sibling' components. Sibling components are defined as
	 * entPhysicalEntries that share the same instance values of each of the
	 * entPhysicalContainedIn and entPhysicalClass objects.
	 * 
	 * An NMS can use this object to identify the relative ordering for all
	 * sibling components of a particular parent (identified by the
	 * entPhysicalContainedIn instance in each sibling entry).
	 * 
	 * If possible, this value should match any external labeling of the
	 * physical component. For example, for a container (e.g., card slot)
	 * labeled as 'slot #3', entPhysicalParentRelPos should have the value '3'.
	 * Note that the entPhysicalEntry for the module plugged in slot 3 should
	 * have an entPhysicalParentRelPos value of '1'.
	 * 
	 * If the physical position of this component does not match any external
	 * numbering or clearly visible ordering, then user documentation or other
	 * external reference material should be used to determine the
	 * parent-relative position. If this is not possible, then the agent should
	 * assign a consistent (but possibly arbitrary) ordering to a given set of
	 * 'sibling' components, perhaps based on internal representation of the
	 * components.
	 * 
	 * 
	 * If the agent cannot determine the parent-relative position for some
	 * reason, or if the associated value of entPhysicalContainedIn is '0', then
	 * the value '-1' is returned. Otherwise, a non-negative integer is
	 * returned, indicating the parent-relative position of this physical
	 * entity.
	 * 
	 * Parent-relative ordering normally starts from '1' and continues to 'N',
	 * where 'N' represents the highest positioned child entity. However, if the
	 * physical entities (e.g., slots) are labeled from a starting position of
	 * zero, then the first sibling should be associated with an
	 * entPhysicalParentRelPos value of '0'. Note that this ordering may be
	 * sparse or dense, depending on agent implementation.
	 * 
	 * The actual values returned are not globally meaningful, as each 'parent'
	 * component may use different numbering algorithms. The ordering is only
	 * meaningful among siblings of the same parent component.
	 * 
	 * The agent should retain parent-relative position values across reboots,
	 * either through algorithmic assignment or use of non-volatile storage.
	 * 
	 * Type Integer32<br>
	 * * Permission read-only<br>
	 * Status current <br>
	 * Range -1 - 2147483647<br>
	 * 
	 */
	public final String entPhysicalParentRelPos = ".1.3.6.1.2.1.47.1.1.1.1.6";

	/**
	 * OID 1.3.6.1.2.1.47.1.1.1.1.11 <br>
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-write <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "The
	 * vendor-specific serial number string for the physical entity. The
	 * preferred value is the serial number string actually printed on the
	 * component itself (if present).
	 * 
	 * On the first instantiation of an physical entity, the value of
	 * entPhysicalSerialNum associated with that entity is set to the correct
	 * vendor-assigned serial number, if this information is available to the
	 * agent. If a serial number is unknown or non-existent, the
	 * entPhysicalSerialNum will be set to a zero-length string instead.
	 * 
	 * Note that implementations that can correctly identify the serial numbers
	 * of all installed physical entities do not need to provide write access to
	 * the entPhysicalSerialNum object. Agents which cannot provide non-volatile
	 * storage for the entPhysicalSerialNum strings are not required to
	 * implement write access for this object.
	 * 
	 * Not every physical component will have a serial number, or even need one.
	 * Physical entities for which the associated value of the entPhysicalIsFRU
	 * object is equal to 'false(2)' (e.g., the repeater ports within a repeater
	 * module), do not need their own unique serial number. An agent does not
	 * have to provide write access for such entities, and may return a
	 * zero-length string.
	 * 
	 * If write access is implemented for an instance of entPhysicalSerialNum,
	 * and a value is written into the instance, the agent must retain the
	 * supplied value in the entPhysicalSerialNum instance (associated with the
	 * same physical entity) for as long as that entity remains instantiated.
	 * This includes instantiations across all re-initializations/reboots of the
	 * network management system, including those resulting in a change of the
	 * physical
	 * 
	 * 
	 * entity's entPhysicalIndex value."
	 */
	public final String entPhysicalSerialNum = ".1.3.6.1.2.1.47.1.1.1.1.11";

	/**
	 * OID 1.3.6.1.2.1.47.1.1.1.1.10 <br>
	 * Type SnmpAdminString <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "The
	 * vendor-specific software revision string for the physical entity.
	 * 
	 * Note that if revision information is stored internally in a
	 * 
	 * 
	 * non-printable (e.g., binary) format, then the agent must convert such
	 * information to a printable format, in an implementation-specific manner.
	 * 
	 * If no specific software programs are associated with the physical
	 * component, or if this information is unknown to the agent, then this
	 * object will contain a zero-length string."
	 */
	public final String entPhysicalSoftwareRev = ".1.3.6.1.2.1.47.1.1.1.1.10";

	/**
	 * "This object contains additional identification information about the
	 * physical entity. The object contains URIs and, therefore, the syntax of
	 * this object must conform to RFC 3986, section 2.
	 * 
	 * Multiple URIs may be present and are separated by white space characters.
	 * Leading and trailing white space characters are ignored.
	 * 
	 * If no additional identification information is known about the physical
	 * entity or supported, the object is not instantiated. A zero length octet
	 * string may also be
	 * 
	 * 
	 * returned in this case."
	 * 
	 * Type OCTET STRING <br>
	 * Permission read-write<br>
	 * Status current
	 * 
	 */
	public final String entPhysicalUris = ".1.3.6.1.2.1.47.1.1.1.1.18";

	/**
	 * 
	 * OID 1.3.6.1.2.1.47.1.1.1.1.3 <br>
	 * Type AutonomousType <br>
	 * 
	 * Permission read-only <br>
	 * Status current <br>
	 * MIB ENTITY-MIB ; - View Supporting Images Description "An indication of
	 * the vendor-specific hardware type of the physical entity. Note that this
	 * is different from the definition of MIB-II's sysObjectID.
	 * 
	 * An agent should set this object to an enterprise-specific registration
	 * identifier value indicating the specific equipment type in detail. The
	 * associated instance of entPhysicalClass is used to indicate the general
	 * type of hardware device.
	 * 
	 * If no vendor-specific registration identifier exists for this physical
	 * entity, or the value is unknown by this agent, then the value { 0 0 } is
	 * returned."
	 * 
	 */
	public final String entPhysicalVendorType = ".1.3.6.1.2.1.47.1.1.1.1.3";
	
	

	private final String physicalClassName[] = new String[] { "", "other", "unknown", "chassis", "backplane", "container",
			"powerSupply", "fan", "sensor", "module", "port", "stack", "cpu" };

	/**
	 * 
	 * @param v
	 * @return
	 */
	public String getPhysicalClass(int v) {
		if (v < 0 || v >= physicalClassName.length) return v + "";
		return physicalClassName[v];
	}
}
