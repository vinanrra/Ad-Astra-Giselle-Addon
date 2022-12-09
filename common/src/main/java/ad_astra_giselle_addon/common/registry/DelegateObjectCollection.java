package ad_astra_giselle_addon.common.registry;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;
import java.util.stream.Stream;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

public class DelegateObjectCollection<T>
{
	private final String modid;
	private final ResourceKey<? extends Registry<T>> key;
	private final Set<DelegateObjectHolder<T>> objects;
	private final Set<DelegateObjectHolder<T>> readonlyObjects;

	public DelegateObjectCollection(String modid, ResourceKey<? extends Registry<T>> key)
	{
		this.modid = modid;
		this.key = key;
		this.objects = new HashSet<>();
		this.readonlyObjects = Collections.unmodifiableSet(this.objects);
	}

	public static <T> DelegateObjectCollection<T> create(String modid, ResourceKey<? extends Registry<T>> key)
	{
		return new DelegateObjectCollection<>(modid, key);
	}

	public void register()
	{
		DelegateRegistry<T> registry = DelegateRegistry.get(this.getKey());

		for (DelegateObjectHolder<T> object : this.getObjects())
		{
			object.register(registry);
		}

	}

	@SuppressWarnings("unchecked")
	public <I extends T> DelegateObjectHolder<I> add(String name, Supplier<? extends I> initializer)
	{
		DelegateObjectHolder<? extends I> object = new DelegateObjectHolder<>(new ResourceLocation(this.getModid(), name), initializer, this.getKey());
		this.objects.add((DelegateObjectHolder<T>) object);
		return (DelegateObjectHolder<I>) object;
	}

	public String getModid()
	{
		return this.modid;
	}

	public ResourceKey<? extends Registry<T>> getKey()
	{
		return this.key;
	}

	public Collection<DelegateObjectHolder<T>> getObjects()
	{
		return this.readonlyObjects;
	}

	public Stream<T> stream()
	{
		return this.getObjects().stream().map(DelegateObjectHolder<T>::get);
	}

	public Collection<T> getValues()
	{
		return this.stream().toList();
	}

}
