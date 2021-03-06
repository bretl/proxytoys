/*
 * (c) 2005, 2009, 2010 ThoughtWorks Ltd
 * All rights reserved.
 *
 * The software in this package is published under the terms of the BSD
 * style license a copy of which has been included with this distribution in
 * the LICENSE.txt file.
 * 
 * Created on 02-Aug-2005
 */
package proxytoys.examples.overview;

import java.util.zip.CRC32;
import java.util.zip.Checksum;

import com.thoughtworks.proxy.factory.CglibProxyFactory;
import com.thoughtworks.proxy.kit.Resetter;
import com.thoughtworks.proxy.toys.pool.Pool;
import com.thoughtworks.proxy.toys.pool.Poolable;


/**
 * @author J&ouml;rg Schaible
 */
public class PoolToyExample {

    public static void packageOverviewExample1() {
        Resetter<Checksum> resetter = new Resetter<Checksum>() {
            public boolean reset(Checksum object) {
                object.reset();
                return true;
            }
        };
        Pool<Checksum> pool = Pool.create(Checksum.class).resettedBy(resetter).build(new CglibProxyFactory());
        pool.add(new CRC32());
        {
            Checksum checksum = pool.get();
            checksum.update("JUnit".getBytes(), 0, 5);
            System.out.println("CRC32 checksum of \"JUnit\": " + checksum.getValue());
        }
        {
            Checksum checksum = pool.get();
            if (checksum == null) {
                System.out.println("No checksum available, force gc ...");
                System.gc();
            }
            checksum = pool.get();
            System.out.println("CRC32 of an resetted checksum: " + checksum.getValue());
            Poolable.class.cast(checksum).returnInstanceToPool();
        }
    }

    public static void main(String[] args) {
        System.out.println();
        System.out.println();
        System.out.println("Running Pool Toy Examples");
        System.out.println();
        System.out.println("Example 1 of Package Overview:");
        packageOverviewExample1();
    }
}
