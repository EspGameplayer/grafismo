import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { ShirtDetailComponent } from './shirt-detail.component';

describe('Shirt Management Detail Component', () => {
  let comp: ShirtDetailComponent;
  let fixture: ComponentFixture<ShirtDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [ShirtDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ shirt: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(ShirtDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(ShirtDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load shirt on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.shirt).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
