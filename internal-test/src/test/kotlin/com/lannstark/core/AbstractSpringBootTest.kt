package com.lannstark.core

import com.lannstark.CleaningSpringBootTest
import com.lansntark.WebApplication
import org.springframework.boot.test.context.SpringBootTest

@SpringBootTest(classes = [WebApplication::class])
abstract class AbstractSpringBootTest : CleaningSpringBootTest()