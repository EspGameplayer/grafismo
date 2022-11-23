import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { GraphicElementPosDetailComponent } from './graphic-element-pos-detail.component';

describe('GraphicElementPos Management Detail Component', () => {
  let comp: GraphicElementPosDetailComponent;
  let fixture: ComponentFixture<GraphicElementPosDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [GraphicElementPosDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ graphicElementPos: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(GraphicElementPosDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(GraphicElementPosDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load graphicElementPos on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.graphicElementPos).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
